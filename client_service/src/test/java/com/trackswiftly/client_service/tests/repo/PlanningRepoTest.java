package com.trackswiftly.client_service.tests.repo;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.trackswiftly.client_service.dao.repositories.PlanningRepo;
import com.trackswiftly.client_service.entities.PlanningEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;



@ExtendWith(MockitoExtension.class)
class PlanningRepoTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<PlanningEntity> typedQuery;
    
    @InjectMocks
    private PlanningRepo planningRepo;

    private PlanningEntity testEntity1;
    private PlanningEntity testEntity2;
    private PlanningEntity testEntity3;


    @BeforeEach
    void setUp() {
        // Set the batch size using reflection to avoid Spring context dependency
        ReflectionTestUtils.setField(planningRepo, "batchSize", 2);
        
        // Create test entities
        testEntity1 = createTestEntity(1L);
        testEntity2 = createTestEntity(2L);
        testEntity3 = createTestEntity(3L);
    }


    private PlanningEntity createTestEntity(Long id) {
        PlanningEntity entity = new PlanningEntity();
        // Use reflection to set ID if the entity has an ID field
        // This ensures each entity is unique
        try {
            ReflectionTestUtils.setField(entity, "id", id);
        } catch (Exception e) {
            // If setting ID fails, that's okay for testing purposes
        }
        return entity;
    }


    @Test
    void testInsertInBatchWithNullEntitiesReturnsNull() {
        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(null);

        // Then
        assertNull(result);
        verify(entityManager, never()).persist(any());
        verify(entityManager, never()).flush();
        verify(entityManager, never()).clear();
    }


    @Test
    void testInsertInBatchWithEmptyListReturnsEmptyList() {
        // Given
        List<PlanningEntity> emptyList = Collections.emptyList();

        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(emptyList);

        // Then
        assertEquals(emptyList, result);
        verify(entityManager, never()).persist(any());
        verify(entityManager, never()).flush();
        verify(entityManager, never()).clear();
    }


    @Test
    void testInsertInBatchWithSingleEntityPersistsWithoutBatching() {
        // Given
        List<PlanningEntity> entities = Arrays.asList(testEntity1);

        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(entities);

        // Then
        assertEquals(entities, result);
        verify(entityManager, times(1)).persist(testEntity1);
        verify(entityManager, times(1)).flush();
        verify(entityManager, times(1)).clear();
    }



    @Test
    void testInsertInBatch_WithExactBatchSize_FlushesOnce() {
        // Given
        List<PlanningEntity> entities = Arrays.asList(testEntity1, testEntity2);

        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(entities);

        // Then
        assertEquals(entities, result);
        // Verify each entity is persisted exactly once
        verify(entityManager).persist(testEntity1);
        verify(entityManager).persist(testEntity2);
        // Verify total persist calls
        verify(entityManager, times(2)).persist(any(PlanningEntity.class));
        // With exact batch size (2), should flush once after the batch is complete
        verify(entityManager, times(1)).flush();
        verify(entityManager, times(1)).clear();
    }


     @Test
    void testInsertInBatchWithMoreThanBatchSizeFlushesMultipleTimes() {
        // Given
        List<PlanningEntity> entities = Arrays.asList(testEntity1, testEntity2, testEntity3);

        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(entities);

        // Then
        assertEquals(entities, result);
        verify(entityManager, times(3)).persist(any(PlanningEntity.class));
        // Should flush twice: once after batch size (2), once for remaining entities
        verify(entityManager, times(2)).flush();
        verify(entityManager, times(2)).clear();
    }


    @Test
    void testInsertInBatchWithLargeBatchHandlesBatchingCorrectly() {
        // Given
        ReflectionTestUtils.setField(planningRepo, "batchSize", 3);
        PlanningEntity entity4 = createTestEntity(4L);
        PlanningEntity entity5 = createTestEntity(5L);
        List<PlanningEntity> entities = Arrays.asList(testEntity1, testEntity2, testEntity3, entity4, entity5);

        // When
        List<PlanningEntity> result = planningRepo.insertInBatch(entities);

        // Then
        assertEquals(entities, result);
        verify(entityManager, times(5)).persist(any(PlanningEntity.class));
        // Should flush twice: once after 3 entities, once for remaining 2
        verify(entityManager, times(2)).flush();
        verify(entityManager, times(2)).clear();
    }


    @Test
    void testFindByIdsAndTimeRange_WithValidParameters_ExecutesQuery() {
        // Given
        OffsetDateTime from = OffsetDateTime.now().minusDays(1);
        OffsetDateTime to = OffsetDateTime.now();
        List<PlanningEntity> expectedResults = Arrays.asList(testEntity1, testEntity2);

        when(entityManager.createQuery(anyString(), eq(PlanningEntity.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter("from", from)).thenReturn(typedQuery);
        when(typedQuery.setParameter("to", to)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedResults);

        // When
        List<PlanningEntity> result = planningRepo.findByTimeRange(from, to);

        // Then
        assertEquals(expectedResults, result);
        verify(entityManager).createQuery("SELECT p FROM PlanningEntity p WHERE p.createdAt BETWEEN :from AND :to", PlanningEntity.class);
        verify(typedQuery).setParameter("from", from);
        verify(typedQuery).setParameter("to", to);
        verify(typedQuery).getResultList();
    }
}