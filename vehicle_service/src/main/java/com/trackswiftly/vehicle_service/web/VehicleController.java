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
import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;

import com.trackswiftly.vehicle_service.services.VehicleService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/vehicles")
@Slf4j
@Validated
public class VehicleController {


    private VehicleService vehicleService ;

    @Autowired
    VehicleController(
        VehicleService vehicleService
    ) {
        this.vehicleService = vehicleService ;
    }
    


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<VehicleResponse> createVehicles(
        @RequestBody @Valid List<VehicleRequest> vehicleRequests
    ) {
        return vehicleService.createVehicles(vehicleRequests);
    }



    @DeleteMapping("/{vehicleIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")   
    public ResponseEntity<OperationResult> deleteVehicles(
        @PathVariable List<Long> vehicleIds
    ) {
        OperationResult result = vehicleService.deleteVehicles(vehicleIds);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{vehicleIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<VehicleResponse>> findVehicles(

        @PathVariable List<Long> vehicleIds
    ) {
        List<VehicleResponse> vehicleResponses = vehicleService.findVehicles(vehicleIds);
        return ResponseEntity.ok(vehicleResponses);
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<VehicleResponse>> getVehiclesWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<VehicleResponse> vehicles = vehicleService.getVehiclesWithPagination(page, pageSize);

        return ResponseEntity.ok(vehicles);
    }



    @PutMapping("/{vehicleIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updateVehiclesInBatch(
        
        @PathVariable List<Long> vehicleIds,

        @Valid @RequestBody VehicleRequest vehicleRequest
    ) {
        OperationResult result = vehicleService.updateVehiclesInBatch(vehicleIds, vehicleRequest) ;
        return ResponseEntity.ok(result);
    }
}
