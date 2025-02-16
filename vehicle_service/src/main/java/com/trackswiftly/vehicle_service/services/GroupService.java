package com.trackswiftly.vehicle_service.services;



import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.annotations.LogUserOperation;
import com.trackswiftly.vehicle_service.dao.repositories.GroupRepo;
import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.mappers.GroupMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class GroupService {
    

    private GroupMapper groupMapper ;

    private GroupRepo groupRepo ;


    @Autowired
    GroupService(
        GroupMapper groupMapper ,
        GroupRepo groupRepo
    ) {

        this.groupMapper = groupMapper ;
        this.groupRepo = groupRepo ;

    }


    public List<GroupResponse> createGroups(List<GroupRequest> groupRequests) {
        log.info("Creating groups in batch...");
        

        List<Group> groups = groupRepo.insertInBatch(groupMapper.toGroupList(groupRequests) ) ;

        log.debug("service : groups : {}" , groups);

        List<GroupResponse> groupResponses = groupMapper.toGroupResponseList(groups);

        log.debug("service : groupResponses : {}" , groupResponses);

        return groupResponses ;
    }


    @LogUserOperation("Delete groups in batch")
    public OperationResult deleteGroups(List<Long> groupIds) {

        if (groupIds == null || groupIds.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = groupRepo.deleteByIds(groupIds); 

        return OperationResult.of(count) ;
    }


    public List<GroupResponse> findGroups(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Group> groups = groupRepo.findByIds(ids);

        return groupMapper.toGroupResponseList(groups);
    }


    public PageDTO<GroupResponse> getGroupsWithPagination(int page, int pageSize) {
        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<GroupResponse> content = groupMapper.toGroupResponseList( // mappe groups to groupsrespones
            groupRepo.findWithPagination(page, pageSize) // get grooup data
        ) ;


        long totalElements = groupRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }


    @LogUserOperation("Update Groups in batch")
    public OperationResult updategroupsInBatch(List<Long> groupIds, GroupRequest group) {
        
        if (groupIds == null || groupIds.isEmpty()) {
            throw new IllegalArgumentException("group IDs list cannot be null or empty");
        }

        if (group == null) {
            throw new IllegalArgumentException("group object cannot be null");
        }

        int count = groupRepo.updateInBatch(groupIds, groupMapper.toGroup(group)) ;

        return OperationResult.of(count);
    }
}
