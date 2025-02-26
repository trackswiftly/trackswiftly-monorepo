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

import com.trackswiftly.vehicle_service.dao.repositories.GroupRepo;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.utils.DBUtiles;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;


@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GroupRepoTest {
    
    
    @Mock
    private EntityManager em;
    
    @InjectMocks
    private GroupRepo groupRepo;
    
    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(groupRepo, "batchSize", 3);
    }


    @Test
    void testInsertInBatch() {
        List<Group> entities = List.of(new Group(), new Group() , new Group());

        groupRepo.insertInBatch(entities);

        verify(em, times(3)).persist(any(Group.class));

        verify(em, times(1)).flush();
        verify(em, times(1)).clear();
    }

    @Test
    void testUpdateInBatch() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Group group = new Group();

        try (MockedStatic<DBUtiles> utilities = Mockito.mockStatic(DBUtiles.class)) {
            Query mockQuery = mock(Query.class);
            utilities.when(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(group, em)).thenReturn(mockQuery);
            when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
            when(mockQuery.executeUpdate()).thenReturn(1);

            int updatedRecords = groupRepo.updateInBatch(ids, group);

            assertEquals(1, updatedRecords);
            verify(mockQuery, times(1)).setParameter("Ids", ids);
            verify(mockQuery, times(1)).executeUpdate();
            utilities.verify(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(group, em), times(1));
        }
    }




    @Test
    void testDeleteByIds_NullOrEmptyIds() {
        // Test with null
        int result = groupRepo.deleteByIds(null);
        assertEquals(0, result);

        // Test with empty list
        result = groupRepo.deleteByIds(Collections.emptyList());
        assertEquals(0, result);

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }


    @Test
    void testDeleteByIds_ValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);

        Query mockQuery = mock(Query.class);

        when(em.createQuery("DELETE FROM Group d WHERE d.id IN :ids")).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.executeUpdate()).thenReturn(ids.size());

        int result = groupRepo.deleteByIds(ids);

        assertEquals(ids.size(), result);

        verify(em).createQuery("DELETE FROM Group d WHERE d.id IN :ids");
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).executeUpdate();
    }



    @Test
    void testFindByIds_NullOrEmptyIds() {
        // Test with null
        List<Group> result = groupRepo.findByIds(null);
        assertTrue(result.isEmpty());

        // Test with empty list
        result = groupRepo.findByIds(Collections.emptyList());
        assertTrue(result.isEmpty());

        // Verify that no interactions with EntityManager occurred
        verifyNoInteractions(em);
    }




    @Test
    void testFindByIds_ValidIds() {
        List<Long> ids = List.of(1L, 2L, 3L);
        List<Group> expectedGroups = List.of(new Group(), new Group(), new Group());

        @SuppressWarnings("unchecked")
        TypedQuery<Group> mockQuery = mock(TypedQuery.class);

        when(em.createQuery("SELECT d FROM Group d WHERE d.id IN :ids", Group.class)).thenReturn(mockQuery);
        when(mockQuery.setParameter("ids", ids)).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(expectedGroups);

        List<Group> result = groupRepo.findByIds(ids);

        assertEquals(expectedGroups.size(), result.size());
        assertEquals(expectedGroups, result);

        verify(em).createQuery("SELECT d FROM Group d WHERE d.id IN :ids", Group.class);
        verify(mockQuery).setParameter("ids", ids);
        verify(mockQuery).getResultList();
    }


}
