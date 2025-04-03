package com.trackswiftly.vehicle_service.tests.repo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.trackswiftly.client_service.dao.repositories.PoiTypeRepo;
import com.trackswiftly.client_service.entities.PoiType;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


@DisabledInNativeImage
class PoiTypeRepoTest {

    @InjectMocks
    private PoiTypeRepo poiTypeRepo;

    @Mock
    private EntityManager em;

    @Mock
    private Query query;


    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<PoiType> typedQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        poiTypeRepo.setBatchSize(2);
    }



    @Test
    void testInsertInBatch() {
        List<PoiType> poiTypes = Arrays.asList(
            PoiType.builder().id(1L).name("Type1").build(),
            PoiType.builder().id(2L).name("Type2").build(),
            PoiType.builder().id(3L).name("Type3").build()
        );

        poiTypeRepo.insertInBatch(poiTypes);

        verify(em, times(3)).persist(any(PoiType.class));
        verify(em, times(2)).flush();
        verify(em, times(2)).clear();
    }



    @Test
    void testDeleteByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(em.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), anyList())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(3);

        int result = poiTypeRepo.deleteByIds(ids);

        assertEquals(3, result);
        verify(query, times(1)).executeUpdate();
    }


    @Test
    void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<PoiType> poiTypes = Arrays.asList(
            PoiType.builder().id(1L).name("Type1").build(),
            PoiType.builder().id(2L).name("Type2").build()
        );

        when(em.createQuery(anyString(), eq(PoiType.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anyList())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(poiTypes);

        List<PoiType> result = poiTypeRepo.findByIds(ids);

        assertEquals(2, result.size());
        verify(typedQuery, times(1)).getResultList();
    }


     @Test
    void testFindWithPagination() {
        List<PoiType> poiTypes = Arrays.asList(
            PoiType.builder().id(1L).name("Type1").build(),
            PoiType.builder().id(2L).name("Type2").build()
        );


        when(em.createQuery(anyString(), eq(PoiType.class))).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(poiTypes);

        List<PoiType> result = poiTypeRepo.findWithPagination(0, 2);

        assertEquals(2, result.size());
        verify(typedQuery, times(1)).getResultList();
    }


    @Test
    void testCount() {
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        Long count = poiTypeRepo.count();

        assertEquals(5L, count);
        verify(countQuery, times(1)).getSingleResult();
    }
}
