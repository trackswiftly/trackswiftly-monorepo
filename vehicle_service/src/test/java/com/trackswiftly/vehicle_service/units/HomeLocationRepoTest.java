package com.trackswiftly.vehicle_service.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.trackswiftly.vehicle_service.dao.repositories.HomeLocationRepo;
import com.trackswiftly.vehicle_service.entities.HomeLocation;
import com.trackswiftly.vehicle_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


@Tag("unit")
@ExtendWith(MockitoExtension.class)
class HomeLocationRepoTest {
    
    
    @Mock
    private EntityManager em;
    
    @InjectMocks
    private HomeLocationRepo homeLocationRepo;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(homeLocationRepo, "batchSize", 3);
    }


    @Test
    void testInsertInBatch() {
        List<HomeLocation> entities = List.of(new HomeLocation(), new HomeLocation() , new HomeLocation());

        homeLocationRepo.insertInBatch(entities);

        verify(em, times(3)).persist(any(HomeLocation.class));

        verify(em, times(1)).flush();
        verify(em, times(1)).clear();
    }

    @Test
    void testUpdateInBatch() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        HomeLocation homeLocation = new HomeLocation();

        try (MockedStatic<DBUtiles> utilities = Mockito.mockStatic(DBUtiles.class)) {
            Query mockQuery = mock(Query.class);
            utilities.when(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(homeLocation, em)).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);

            int updatedRecords = homeLocationRepo.updateInBatch(ids, homeLocation);

            assertEquals(1, updatedRecords);
            verify(mockQuery, times(1)).setParameter("Ids", ids);
            verify(mockQuery, times(1)).executeUpdate();
            utilities.verify(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(homeLocation, em), times(1));
        }
    }




    @Test
    void testDeleteByIdsNullOrEmptyIds() {
        // Test with null
        int result = homeLocationRepo.deleteByIds(null);
        assertEquals(0, result);

        // Test with empty list
        result = homeLocationRepo.deleteByIds(Collections.emptyList());
        assertEquals(0, result);

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }


    @Test
    void testDeleteByIdsValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);

        Query mockQuery = mock(Query.class);

        when(em.createQuery("DELETE FROM HomeLocation d WHERE d.id IN :ids")).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(ids.size());

        int result = homeLocationRepo.deleteByIds(ids);

        assertEquals(ids.size(), result);

        verify(em).createQuery("DELETE FROM HomeLocation d WHERE d.id IN :ids");
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).executeUpdate();
    }



    @Test
    void testFindByIds_NullOrEmptyIds() {
        // Test with null
        List<HomeLocation> result = homeLocationRepo.findByIds(null);
        assertTrue(result.isEmpty());

        // Test with empty list
        result = homeLocationRepo.findByIds(Collections.emptyList());
        assertTrue(result.isEmpty());

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }




    @Test
    void testFindByIds_ValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<HomeLocation> expectedHomeLocations = List.of(new HomeLocation(), new HomeLocation(), new HomeLocation());

        @SuppressWarnings("unchecked")
        TypedQuery<HomeLocation> mockQuery = mock(TypedQuery.class);

        when(em.createQuery("SELECT d FROM HomeLocation d WHERE d.id IN :ids", HomeLocation.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(expectedHomeLocations);

        List<HomeLocation> result = homeLocationRepo.findByIds(ids);

        assertEquals(expectedHomeLocations.size(), result.size());
        assertEquals(expectedHomeLocations, result);

        verify(em).createQuery("SELECT d FROM HomeLocation d WHERE d.id IN :ids", HomeLocation.class);
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).getResultList();
    }


}
