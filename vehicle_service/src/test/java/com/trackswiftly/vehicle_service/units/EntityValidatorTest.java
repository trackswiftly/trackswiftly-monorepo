package com.trackswiftly.vehicle_service.units;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.exception.UnableToProccessIteamException;
import com.trackswiftly.vehicle_service.validators.EntityValidator;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;


/**
 * 
 * @ExtendWith(MockitoExtension.class)
 * It allows the use of Mockito annotations like @Mock, @InjectMocks,
 * and @Captor without the need for manual initialization. 
 * This streamlines the setup process for unit tests.
 * 
 */
@Tag("unit")
@ExtendWith(MockitoExtension.class)
class EntityValidatorTest {
    
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EntityValidator entityValidator;





    @Test
    void testValidateVehicleRequestWithEmptyList() {
        List<VehicleRequest> requests = Collections.emptyList();

        entityValidator.validateVehicleRequest(requests);

        verify(entityManager, never()).createQuery(anyString());
    }



    @Test
    void testValidateVehicleRequestWithValidIds() {
        List<VehicleRequest> requests = List.of(
            VehicleRequest.builder()
                .vehicleTypeId(1L)
                .licensePlate("ABC123")
                .mileage(10000)
                .purchaseDate(null)
                .modelId(1L)
                .homeLocationId(1L)
                .vehicleGroupId(1L)
                .build()
        );

        // Mock the behavior of the EntityManager
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(1L);

        entityValidator.validateVehicleRequest(requests);

        // Verify that the EntityManager was called with the expected query
        verify(entityManager , times(4)).createQuery(anyString());
        verify(mockQuery , times(4)).setParameter("ids",List.of(1L));
        verify(mockQuery  , times(4)).getSingleResult();
    }




    @Test
    void testValidateVehicleRequestWithNonExistentEntity() {
        List<VehicleRequest> requests = List.of(
            VehicleRequest.builder()
                .vehicleTypeId(999L)
                .licensePlate("ABC123")
                .mileage(10000)
                .purchaseDate(null)
                .modelId(999L)
                .homeLocationId(999L)
                .vehicleGroupId(999L)
                .build()
        );

        // Mock the behavior of the EntityManager
        Query mockQuery = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(0L);

        // Expect an exception to be thrown
        assertThrows(UnableToProccessIteamException.class, () -> {
            entityValidator.validateVehicleRequest(requests);
        });

        // Verify that the EntityManager was called with the expected query
        verify(entityManager ).createQuery(anyString());
        verify(mockQuery ).setParameter("ids", List.of(999L));
        verify(mockQuery).getSingleResult();
    }
}
