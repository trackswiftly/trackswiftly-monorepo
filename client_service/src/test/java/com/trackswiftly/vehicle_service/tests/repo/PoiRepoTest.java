package com.trackswiftly.vehicle_service.tests.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.client_service.dao.repositories.PoiRepo;
import com.trackswiftly.client_service.entities.Poi;
import com.trackswiftly.client_service.utils.DBUtiles;
import com.trackswiftly.utils.base.utils.TenantContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@ExtendWith(MockitoExtension.class)
class PoiRepoTest {


     @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Poi> typedQuery;

    @InjectMocks
    private PoiRepo poiRepo;


    private List<Poi> poiList;
    private List<Long> ids;


    @BeforeEach
    void setUp() {
        poiRepo.setBatchSize(2);

        TenantContext.setTenantId("test-tenant");

        Poi poi1 = Poi.builder()
                .id(1L)
                .name("Location 1")
                .build();
        Poi poi2 = Poi.builder()
                .id(2L)
                .name("Location 2")
                .build();
        poiList = Arrays.asList(poi1, poi2);
        ids = Arrays.asList(1L, 2L);
    }


    @Test
    void testInsertInBatch() {
        doNothing().when(entityManager).persist(any(Poi.class));
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).clear();

        List<Poi> result = poiRepo.insertInBatch(poiList);

        assertEquals(2, result.size());
        verify(entityManager, times(2)).persist(any(Poi.class));
        verify(entityManager, times(1)).flush();
        verify(entityManager, times(1)).clear();
    }


    @Test
    void testDeleteByIds() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(eq("ids"), anyList())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(2);

        int deletedCount = poiRepo.deleteByIds(ids);

        assertEquals(2, deletedCount);
        verify(entityManager, times(1)).createQuery(anyString());
        verify(query, times(1)).executeUpdate();
    }


    @Test
    void testFindByIds() {
        when(entityManager.createQuery(anyString(), eq(Poi.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(eq("ids"), anyList())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(poiList);

        List<Poi> result = poiRepo.findByIds(ids);

        assertEquals(2, result.size());
        verify(entityManager, times(1)).createQuery(anyString(), eq(Poi.class));
        verify(typedQuery, times(1)).getResultList();
    }


    @Test
    void testFindWithPagination() {
        when(entityManager.createQuery(anyString(), eq(Poi.class))).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(poiList);

        List<Poi> result = poiRepo.findWithPagination(0, 2);

        assertEquals(2, result.size());
        verify(typedQuery, times(1)).setFirstResult(0);
        verify(typedQuery, times(1)).setMaxResults(2);
        verify(typedQuery, times(1)).getResultList();
    }


    @Test
    void testCount() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        Long count = poiRepo.count();

        assertEquals(5L, count);
        verify(entityManager, times(1)).createQuery(anyString(), eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }


    @Test
    void testUpdateInBatch() {
        // Create a valid Poi entity
        Poi poiEntity = Poi.builder()
                .address("Updated Address")
                .build();

        // Stub the query behavior: any call to setParameter returns the query itself.
        when(query.setParameter(anyString(), any())).thenReturn(query);

        when(query.executeUpdate()).thenReturn(2);
        // Stub flush() and clear() on the entity manager (void methods)
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).clear();

        // Use static mocking for DBUtiles.buildJPQLQueryDynamicallyForUpdate
        try (MockedStatic<DBUtiles> mockedStatic = mockStatic(DBUtiles.class)) {
            mockedStatic.when(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(poiEntity, entityManager))
                    .thenReturn(query);

            // Call updateInBatch
            int totalUpdated = poiRepo.updateInBatch(ids, poiEntity);

            // We expect two batches: first with 2 IDs and second with 1 ID, each returning 1 update.
            // Total updated records should be 1 + 1 = 2.
            assertEquals(2, totalUpdated);

            // Verify that setParameter was called with the correct sublists.
            verify(query, times(1)).setParameter("Ids", ids.subList(0, 2));

            // Verify that executeUpdate was called twice.
            verify(query, times(1)).executeUpdate();
            // Verify that flush and clear are each called once per batch.
            verify(entityManager, times(1)).flush();
            verify(entityManager, times(1)).clear();
        }
    }



    @Test
    void testCountBasedOnIds() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter(eq("ids"), anySet())).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(3L);

        long count = poiRepo.countBasedOnIds(Poi.class, new HashSet<>(ids));

        assertEquals(3L, count);
        verify(entityManager, times(1)).createQuery(anyString(), eq(Long.class));
        verify(countQuery, times(1)).getSingleResult();
    }


    
}
