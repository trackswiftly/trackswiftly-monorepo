package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.entities.Group;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class GroupMapper {



    public List<GroupResponse> toGroupResponseList(List<Group> groups) {

        log.debug("group :{} " , groups);

        List<GroupResponse> groupResponses = groups.stream()
                                            .map(this::toGroupResponse)
                                            .toList() ;
        
        log.debug("group responses :{} " , groupResponses);
        return groupResponses;
    }


    public List<Group> toGroupList(List<GroupRequest> groupRequests) {
        return groupRequests.stream()
                .map(this::toGroup)
                .toList();
    }
    



    public GroupResponse toGroupResponse(Group group) {
        return GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .updatedAt(group.getUpdatedAt())
                .createdAt(group.getCreatedAt())
                .build();
    }
    

    public Group toGroup(GroupRequest groupRequest) {
        return Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .build();
    }
}
