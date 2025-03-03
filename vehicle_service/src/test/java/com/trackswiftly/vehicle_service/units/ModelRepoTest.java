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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import com.trackswiftly.vehicle_service.dao.repositories.ModelRepo;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


// @Tag("unit")
class ModelRepoTest {
    
    
    @Mock
    private EntityManager em;
    
    @InjectMocks
    private ModelRepo modelRepo;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(modelRepo, "batchSize", 3);
    }


    @Test
    void testInsertInBatch() {
        List<Model> entities = List.of(new Model(), new Model() , new Model());

        modelRepo.insertInBatch(entities);

        verify(em, times(3)).persist(any(Model.class));

        verify(em, times(1)).flush();
        verify(em, times(1)).clear();
    }

    @Test
    void testUpdateInBatch() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Model model = new Model();

        try (MockedStatic<DBUtiles> utilities = Mockito.mockStatic(DBUtiles.class)) {
            Query mockQuery = mock(Query.class);
            utilities.when(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(model, em)).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);

            int updatedRecords = modelRepo.updateInBatch(ids, model);

            assertEquals(1, updatedRecords);
            verify(mockQuery, times(1)).setParameter("Ids", ids);
            verify(mockQuery, times(1)).executeUpdate();
            utilities.verify(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(model, em), times(1));
        }
    }




    @Test
    void testDeleteByIds_NullOrEmptyIds() {
        // Test with null
        int result = modelRepo.deleteByIds(null);
        assertEquals(0, result);

        // Test with empty list
        result = modelRepo.deleteByIds(Collections.emptyList());
        assertEquals(0, result);

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }


    @Test
    void testDeleteByIds_ValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);

        Query mockQuery = mock(Query.class);

        when(em.createQuery("DELETE FROM Model d WHERE d.id IN :ids")).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(ids.size());

        int result = modelRepo.deleteByIds(ids);

        assertEquals(ids.size(), result);

        verify(em).createQuery("DELETE FROM Model d WHERE d.id IN :ids");
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).executeUpdate();
    }



    @Test
    void testFindByIds_NullOrEmptyIds() {
        // Test with null
        List<Model> result = modelRepo.findByIds(null);
        assertTrue(result.isEmpty());

        // Test with empty list
        result = modelRepo.findByIds(Collections.emptyList());
        assertTrue(result.isEmpty());

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }




    @Test
    void testFindByIds_ValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<Model> expectedModels = List.of(new Model(), new Model(), new Model());

        @SuppressWarnings("unchecked")
        TypedQuery<Model> mockQuery = mock(TypedQuery.class);

        when(em.createQuery("SELECT d FROM Model d WHERE d.id IN :ids", Model.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(expectedModels);

        List<Model> result = modelRepo.findByIds(ids);

        assertEquals(expectedModels.size(), result.size());
        assertEquals(expectedModels, result);

        verify(em).createQuery("SELECT d FROM Model d WHERE d.id IN :ids", Model.class);
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).getResultList();
    }


}
