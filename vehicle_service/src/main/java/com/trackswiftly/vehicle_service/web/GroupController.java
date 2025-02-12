package com.trackswiftly.vehicle_service.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trackswiftly.vehicle_service.dtos.GroupRequest;
import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.services.GroupService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/groups")
@Slf4j
public class GroupController {


    private GroupService groupService ;

    @Autowired
    GroupController (
        GroupService groupService
    ) {
        this.groupService = groupService ;
    }
    


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<GroupResponse> createGroups(
        @RequestBody @Valid List<GroupRequest> groupRequests
    ) {
        List<GroupResponse> responses = groupService.createGroups(groupRequests);

        log.debug("groups {}" , responses);
        
        return responses ;
    }


    @DeleteMapping("/{groupIds}")   
    public ResponseEntity<OperationResult> deleteGroups(
        @Parameter(
            description = "Comma-separated list of group IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> groupIds
    ) {
        OperationResult result = groupService.deleteGroups(groupIds);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{groupIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<GroupResponse>> findgroups(
        @Parameter(
            description = "Comma-separated list of group IDs to be fetched",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> groupIds
    ) {
        List<GroupResponse> groups = groupService.findGroups(groupIds);
        return ResponseEntity.ok(groups);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<GroupResponse>> getGroupsWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<GroupResponse> groups = groupService.getGroupsWithPagination(page, pageSize);

        return ResponseEntity.ok(groups);
    }


    @PutMapping
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updategroupsInBatch(
        @Parameter(
            description = "Comma-separated list of group IDs to be updated",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @RequestParam List<Long> groupIds,
        @Parameter(
            description = "group object containing the fields to update",
            required = true
        )
        @Valid @RequestBody GroupRequest group
    ) {
        OperationResult result = groupService.updategroupsInBatch(groupIds, group);
        return ResponseEntity.ok(result);
    }
}
