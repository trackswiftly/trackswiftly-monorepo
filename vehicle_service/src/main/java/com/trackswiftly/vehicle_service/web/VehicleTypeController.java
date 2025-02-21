package com.trackswiftly.vehicle_service.web;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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


import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.VehicleTypeRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleTypeResponse;
import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.services.VehicleTypeService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/types")
@Slf4j
@Validated
public class VehicleTypeController {


    private VehicleTypeService vehicleTypeService ;


    @Autowired
    VehicleTypeController(
        VehicleTypeService vehicleTypeService
    ) {
        this.vehicleTypeService = vehicleTypeService ;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<VehicleTypeResponse> createvehicleTypes(
        @RequestBody @Valid List<VehicleTypeRequest> vehicleTypeRequests
    ) {
        List<VehicleTypeResponse> responses = vehicleTypeService.createVehicleTypes(vehicleTypeRequests);

        log.debug("vehicleTypes {}" , responses);
        
        return responses ;
    }



    @DeleteMapping("/{typeIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> deleteVehicleTypes(
        @Parameter(
            description = "Comma-separated list of VehicleType IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> typeIds
    ) {
        OperationResult result = vehicleTypeService.deleteVehicleTypes(typeIds);
        return ResponseEntity.ok(result);
    }




    @GetMapping("/{typeIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<VehicleTypeResponse>> findVehicleType(
        @Parameter(
            description = "Comma-separated list of group IDs to be fetched",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> typeIds
    ) {
        List<VehicleTypeResponse> vehicleTypeResponses = vehicleTypeService.findVehicleTypes(typeIds);

        return ResponseEntity.ok(vehicleTypeResponses);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<VehicleTypeResponse>> getVehicleTypeWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<VehicleTypeResponse> vehicleTypeResponses = vehicleTypeService.getVehicleTypesWithPagination(page, pageSize);

        return ResponseEntity.ok(vehicleTypeResponses);
    }



    @PutMapping("/{typeIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updateVehicleTypesInBatch(
        @Parameter(
            description = "Comma-separated list of group IDs to be updated",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> typeIds,
        @Parameter(
            description = "group object containing the fields to update",
            required = true
        )
        @Valid @RequestBody VehicleTypeRequest vehicleTypeRequest
    ) {
        OperationResult result = vehicleTypeService.updateVehicleTypesInBatch(typeIds, vehicleTypeRequest);
        return ResponseEntity.ok(result);
    }
    
}
