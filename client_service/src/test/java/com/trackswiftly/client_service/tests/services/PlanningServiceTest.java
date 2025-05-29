package com.trackswiftly.client_service.tests.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.client_service.clients.PlannerClient;
import com.trackswiftly.client_service.dao.repositories.PlanningRepo;
import com.trackswiftly.client_service.dtos.routing.PlanningRequest;
import com.trackswiftly.client_service.dtos.routing.PlanningResponse;
import com.trackswiftly.client_service.entities.PlanningEntity;
import com.trackswiftly.client_service.dtos.PlanningEntityResponse;
import com.trackswiftly.client_service.mappers.PlanningMapper;
import com.trackswiftly.client_service.services.PlanningService;

@ExtendWith(MockitoExtension.class)
class PlanningServiceTest {

    @Mock
    private PlanningRepo planningRepo;

    @Mock
    private PlannerClient plannerClient;

    @Mock
    private PlanningMapper planningMapper;

    @InjectMocks
    private PlanningService planningService;


    private PlanningRequest testPlanningRequest;
    private PlanningResponse testPlanningResponse;
    private PlanningEntity testPlanningEntity;
    private PlanningEntityResponse testPlanningEntityResponse;



    @BeforeEach
    void setUp() {
        // Initialize test data with concrete objects to avoid reflection issues
        testPlanningRequest = createTestPlanningRequest();
        testPlanningResponse = createTestPlanningResponse();
        testPlanningEntity = createTestPlanningEntity();
        testPlanningEntityResponse = createTestPlanningEntityResponse();
    }




    @Test
    void createPlanningEntities_WithValidRequest_ShouldReturnPlanningEntityResponses() {
        // Given
        List<PlanningRequest> planningRequests = Arrays.asList(testPlanningRequest);
        List<PlanningEntity> savedEntities = Arrays.asList(testPlanningEntity);
        List<PlanningEntityResponse> expectedResponses = Arrays.asList(testPlanningEntityResponse);

        // Configure mocks explicitly for native compilation
        when(plannerClient.createOptimizationPlan(testPlanningRequest))
            .thenReturn(testPlanningResponse);
        
        when(planningRepo.insertInBatch(anyList()))
            .thenReturn(savedEntities);
        
        when(planningMapper.toPlanningEntityResponseList(savedEntities))
            .thenReturn(expectedResponses);

        // When
        List<PlanningEntityResponse> result = planningService.createPlanningEnitities(planningRequests);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPlanningEntityResponse, result.get(0));

        // Verify interactions
        verify(plannerClient, times(1)).createOptimizationPlan(testPlanningRequest);
        verify(planningRepo, times(1)).insertInBatch(argThat(entities -> 
            entities.size() == 1 && 
            entities.get(0).getPlanningData().equals(testPlanningResponse)
        ));
        verify(planningMapper, times(1)).toPlanningEntityResponseList(savedEntities);
    }



    @Test
    void createPlanningEntities_WithEmptyList_ShouldThrowException() {
        // Given
        List<PlanningRequest> emptyRequests = Collections.emptyList();

        // When & Then
        assertThrows(IndexOutOfBoundsException.class, () -> 
            planningService.createPlanningEnitities(emptyRequests)
        );

        // Verify no interactions with dependencies
        verifyNoInteractions(plannerClient, planningRepo, planningMapper);
    }



    @Test
    void createPlanningEntities_WhenPlannerClientFails_ShouldPropagateException() {
        // Given
        List<PlanningRequest> planningRequests = Arrays.asList(testPlanningRequest);
        RuntimeException clientException = new RuntimeException("Planner service unavailable");

        when(plannerClient.createOptimizationPlan(testPlanningRequest))
            .thenThrow(clientException);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            planningService.createPlanningEnitities(planningRequests)
        );

        assertEquals("Planner service unavailable", thrown.getMessage());
        verify(plannerClient, times(1)).createOptimizationPlan(testPlanningRequest);
        verifyNoInteractions(planningRepo, planningMapper);
    }



    @Test
    void findByTimeRange_WithValidTimeRange_ShouldReturnPlanningEntityResponses() {
        // Given
        long fromEpoch = 1640995200L; // 2022-01-01 00:00:00 UTC
        long toEpoch = 1641081600L;   // 2022-01-02 00:00:00 UTC
        
        OffsetDateTime expectedFromTime = Instant.ofEpochSecond(fromEpoch).atOffset(ZoneOffset.UTC);
        OffsetDateTime expectedToTime = Instant.ofEpochSecond(toEpoch).atOffset(ZoneOffset.UTC);
        
        List<PlanningEntity> foundEntities = Arrays.asList(testPlanningEntity);
        List<PlanningEntityResponse> expectedResponses = Arrays.asList(testPlanningEntityResponse);

        when(planningRepo.findByTimeRange(expectedFromTime, expectedToTime))
            .thenReturn(foundEntities);
        
        when(planningMapper.toPlanningEntityResponseList(foundEntities))
            .thenReturn(expectedResponses);

        // When
        List<PlanningEntityResponse> result = planningService.findByTimeRange(fromEpoch, toEpoch);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPlanningEntityResponse, result.get(0));

        verify(planningRepo, times(1)).findByTimeRange(expectedFromTime, expectedToTime);
        verify(planningMapper, times(1)).toPlanningEntityResponseList(foundEntities);
    }



    @Test
    void findByTimeRange_WithEqualFromAndToTimes_ShouldHandleCorrectly() {
        // Given
        long sameTime = 1640995200L;
        OffsetDateTime expectedTime = Instant.ofEpochSecond(sameTime).atOffset(ZoneOffset.UTC);
        
        List<PlanningEntity> emptyResult = Collections.emptyList();
        List<PlanningEntityResponse> emptyResponse = Collections.emptyList();

        when(planningRepo.findByTimeRange(expectedTime, expectedTime))
            .thenReturn(emptyResult);
        
        when(planningMapper.toPlanningEntityResponseList(emptyResult))
            .thenReturn(emptyResponse);

        // When
        List<PlanningEntityResponse> result = planningService.findByTimeRange(sameTime, sameTime);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(planningRepo, times(1)).findByTimeRange(expectedTime, expectedTime);
        verify(planningMapper, times(1)).toPlanningEntityResponseList(emptyResult);
    }



    @Test
    void findByTimeRange_WhenRepositoryFails_ShouldPropagateException() {
        // Given
        long fromEpoch = 1640995200L;
        long toEpoch = 1641081600L;
        
        OffsetDateTime expectedFromTime = Instant.ofEpochSecond(fromEpoch).atOffset(ZoneOffset.UTC);
        OffsetDateTime expectedToTime = Instant.ofEpochSecond(toEpoch).atOffset(ZoneOffset.UTC);
        
        RuntimeException repoException = new RuntimeException("Database connection failed");

        when(planningRepo.findByTimeRange(expectedFromTime, expectedToTime))
            .thenThrow(repoException);

        // When & Then
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> 
            planningService.findByTimeRange(fromEpoch, toEpoch)
        );

        assertEquals("Database connection failed", thrown.getMessage());
        verify(planningRepo, times(1)).findByTimeRange(expectedFromTime, expectedToTime);
        verifyNoInteractions(planningMapper);
    }




    
    // Helper methods to create test data - concrete objects for better native compilation
    private PlanningRequest createTestPlanningRequest() {
        // Create a concrete PlanningRequest object
        // Adjust based on your actual PlanningRequest structure
        return PlanningRequest.builder()
            .build(); // Add required fields based on your DTO
    }

    private PlanningResponse createTestPlanningResponse() {
        // Create a concrete PlanningResponse object
        // Adjust based on your actual PlanningResponse structure
        return PlanningResponse.builder()
            .build(); // Add required fields based on your DTO
    }

    private PlanningEntity createTestPlanningEntity() {
        return PlanningEntity.builder()
            .planningData(testPlanningResponse)
            .build();
    }

    private PlanningEntityResponse createTestPlanningEntityResponse() {
        // Create a concrete PlanningEntityResponse object
        // Adjust based on your actual PlanningEntityResponse structure
        return PlanningEntityResponse.builder()
            .build(); // Add required fields based on your DTO
    }
    
}
