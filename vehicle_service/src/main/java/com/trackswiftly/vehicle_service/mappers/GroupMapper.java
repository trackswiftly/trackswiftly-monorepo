package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.entities.Group;


@Component
public class GroupMapper {



    public List<GroupResponse> toGroupResponseList(List<Group> groups) {
        return groups.stream()
                .map(this::toGroupResponse)
                .toList();
    }


    public List<Group> toGroupList(List<GroupRequest> groupRequests) {
        return groupRequests.stream()
                .map(this::toGroup)
                .toList();
    }
    



    private GroupResponse toGroupResponse(Group group) {
        return GroupResponse.builder()
                .name(group.getName())
                .description(group.getDescription())
                .updatedAt(group.getUpdatedAt())
                .createdAt(group.getCreatedAt())
                .build();
    }
    

    private Group toGroup(GroupRequest groupRequest) {
        return Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .build();
    }
}
