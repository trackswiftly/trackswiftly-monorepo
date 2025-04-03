package com.trackswiftly.vehicle_service.tests.repo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.trackswiftly.client_service.dao.repositories.GroupRepo;
import com.trackswiftly.client_service.entities.Group;
import com.trackswiftly.client_service.utils.DBUtiles;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupRepoTest {


    @Mock
    private EntityManager entityManager;

    @Mock
    private Query query;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Group> typedQuery;

    @InjectMocks
    private GroupRepo groupRepo;

    private List<Long> ids;
    private List<Group> groups;


    @BeforeEach
    void setUp() {
        // Simulated batch size
        groupRepo.setBatchSize(2); 

        // Sample data
        ids = Arrays.asList(1L, 2L, 3L);
        groups = Arrays.asList(
            Group.builder().id(1L).name("Group A").build(),
            Group.builder().id(2L).name("Group B").build(),
            Group.builder().id(3L).name("Group C").build()
        );
    }


    @Test
    void testInsertInBatch() {
        doNothing().when(entityManager).persist(any(Group.class));
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).clear();

        List<Group> insertedGroups = groupRepo.insertInBatch(groups);

        assertEquals(groups, insertedGroups);
        verify(entityManager, times(3)).persist(any(Group.class));
        verify(entityManager, times(2)).flush();
        verify(entityManager, times(2)).clear();
    }


    @Test
    void testDeleteByIds() {
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(3);

        int deletedCount = groupRepo.deleteByIds(ids);

        assertEquals(3, deletedCount);
        verify(query).setParameter("ids", ids);
        verify(query).executeUpdate();
    }

    @Test
    void testFindByIds() {
        when(entityManager.createQuery(anyString(), eq(Group.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(groups);

        List<Group> foundGroups = groupRepo.findByIds(ids);

        assertEquals(groups, foundGroups);
        verify(typedQuery).setParameter("ids", ids);
        verify(typedQuery).getResultList();
    }


    @Test
    void testFindWithPagination() {
        when(entityManager.createQuery(anyString(), eq(Group.class))).thenReturn(typedQuery);
        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(groups);

        List<Group> paginatedGroups = groupRepo.findWithPagination(1, 2);

        assertEquals(groups, paginatedGroups);
        verify(typedQuery).setFirstResult(2);
        verify(typedQuery).setMaxResults(2);
        verify(typedQuery).getResultList();
    }



    @Test
    void testCount() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(10L);

        Long count = groupRepo.count();

        assertEquals(10L, count);
        verify(countQuery, times(1)).getSingleResult();
    }


    @Test
    void testUpdateInBatch() {
        Group updatedGroup = new Group();
        updatedGroup.setName("Updated Group");

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);
        doNothing().when(entityManager).flush();
        doNothing().when(entityManager).clear();

        try (MockedStatic<DBUtiles> mockedStatic = mockStatic(DBUtiles.class)) {
            mockedStatic.when(() -> DBUtiles.buildJPQLQueryDynamicallyForUpdate(updatedGroup, entityManager))
                        .thenReturn(query);

            int updatedCount = groupRepo.updateInBatch(ids, updatedGroup);

            assertEquals(2, updatedCount);
            verify(query, times(1)).setParameter("Ids", ids.subList(0, 2));
            verify(query, times(1)).setParameter("Ids", ids.subList(2, 3));
            verify(query, times(2)).executeUpdate();
            verify(entityManager, times(2)).flush();
            verify(entityManager, times(2)).clear();
        }
    }

    
}
