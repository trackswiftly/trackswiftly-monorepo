package com.trackswiftly.vehicle_service.services;



import java.util.Collections;
import java.util.List;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.annotations.LogUserOperation;
import com.trackswiftly.vehicle_service.dao.repositories.GroupRepo;
import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
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


    @LogUserOperation
    public List<GroupResponse> createGroups(List<GroupRequest> groupRequests) {
        log.info("Creating groups in batch...");

        return groupMapper.toGroupResponseList( // map groups to groupResponses
            groupRepo.insertInBatch( // insert groups

                groupMapper.toGroupList( // map grouprequests to group entities
                    groupRequests // grouprequestes
                )
            )
        );
    }
}
