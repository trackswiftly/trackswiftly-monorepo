package com.trackswiftly.vehicle_service.tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trackswiftly.client_service.dao.repositories.GroupRepo;
import com.trackswiftly.client_service.dtos.GroupRequest;
import com.trackswiftly.client_service.dtos.GroupResponse;
import com.trackswiftly.client_service.mappers.GroupMapper;
import com.trackswiftly.client_service.services.GroupService;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;
import com.trackswiftly.client_service.entities.Group;


@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    private static final String GROUP1_NAME = "Group1";
    private static final String GROUP2_NAME = "Group2";

    @InjectMocks
    private GroupService groupService;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private GroupRepo groupRepo;



    private List<Group> groups;
    private List<GroupRequest> groupRequests;
    private List<GroupResponse> groupResponses;

    @BeforeEach
    void setUp() {
        groups = Arrays.asList(
            Group.builder().id(1L).name(GROUP1_NAME).build(),
            Group.builder().id(2L).name(GROUP2_NAME).build()
        );
        groupRequests = Arrays.asList(
            GroupRequest.builder().name(GROUP1_NAME).build(),
            GroupRequest.builder().name(GROUP2_NAME).build()
        );
        groupResponses = Arrays.asList(
            GroupResponse.builder().id(1L).name(GROUP1_NAME).build(),
            GroupResponse.builder().id(2L).name(GROUP2_NAME).build()
        );
    }



    @Test
    void testCreateGroups() {
        when(groupMapper.toGroupList(groupRequests)).thenReturn(groups);
        when(groupRepo.insertInBatch(groups)).thenReturn(groups);
        when(groupMapper.toGroupResponseList(groups)).thenReturn(groupResponses);
        
        List<GroupResponse> result = groupService.createGroups(groupRequests);
        
        assertEquals(groupResponses, result);
        verify(groupRepo, times(1)).insertInBatch(groups);
    }



    @Test
    void testDeleteGroups() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(groupRepo.deleteByIds(ids)).thenReturn(2);
        
        OperationResult result = groupService.deleteGroups(ids);
        
        assertEquals(2, result.affectedRecords());
        verify(groupRepo, times(1)).deleteByIds(ids);
    }



    @Test
    void testFindGroups() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(groupRepo.findByIds(ids)).thenReturn(groups);
        when(groupMapper.toGroupResponseList(groups)).thenReturn(groupResponses);
        
        List<GroupResponse> result = groupService.findGroups(ids);
        
        assertEquals(groupResponses, result);
        verify(groupRepo, times(1)).findByIds(ids);
    }



    @Test
    void testGetGroupsWithPagination() {
        int page = 0, pageSize = 2;
        when(groupRepo.findWithPagination(page, pageSize)).thenReturn(groups);
        when(groupMapper.toGroupResponseList(groups)).thenReturn(groupResponses);
        when(groupRepo.count()).thenReturn(10L);
        
        PageDTO<GroupResponse> result = groupService.getGroupsWithPagination(page, pageSize);
        
        assertEquals(10L, result.getTotalElements());
        assertEquals(5, result.getTotalPages());
        verify(groupRepo, times(1)).findWithPagination(page, pageSize);
    }



    @Test
    void testUpdateGroupsInBatch() {
        List<Long> ids = Arrays.asList(1L, 2L);
        GroupRequest request = GroupRequest.builder().name("UpdatedGroup").build();
        Group updatedGroup = Group.builder().name("UpdatedGroup").build();
        
        when(groupMapper.toGroup(request)).thenReturn(updatedGroup);
        when(groupRepo.updateInBatch(ids, updatedGroup)).thenReturn(2);
        
        OperationResult result = groupService.updategroupsInBatch(ids, request);
        
        assertEquals(2, result.affectedRecords());
        verify(groupRepo, times(1)).updateInBatch(ids, updatedGroup);
    }


    
}
