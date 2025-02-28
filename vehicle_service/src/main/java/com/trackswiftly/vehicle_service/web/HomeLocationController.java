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

import com.trackswiftly.vehicle_service.dtos.HomeLocationRequestDTO;
import com.trackswiftly.vehicle_service.dtos.HomeLocationResponseDTO;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.services.HomeLocationService;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/homelocations")
@Slf4j
@Validated
public class HomeLocationController {
    
    private HomeLocationService homeLocationService ;

    @Autowired
    HomeLocationController(
        HomeLocationService homeLocationService
    ){
        this.homeLocationService = homeLocationService ;
    }



    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<HomeLocationResponseDTO> createHomeLocations(
        @RequestBody @Valid List<HomeLocationRequestDTO> homeLocationRequestDTOs
    ) {
        return homeLocationService.createHomeLocations(homeLocationRequestDTOs);
    }


    @DeleteMapping("/{homelocationIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")   
    public ResponseEntity<OperationResult> deleteHomeLocations(
        @Parameter(
            description = "Comma-separated list of HomeLocation IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> homelocationIds
    ) {
        OperationResult result = homeLocationService.deleteHomeLocations(homelocationIds);
        return ResponseEntity.ok(result);
    }



    @GetMapping("/{homelocationIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<HomeLocationResponseDTO>> findHomelocations(
        @Parameter(
            description = "Comma-separated list of Homelocation IDs to be fetched",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> homelocationIds
    ) {
        List<HomeLocationResponseDTO> homeLocationResponseDTOs = homeLocationService.findhomeLocations(homelocationIds);
        return ResponseEntity.ok(homeLocationResponseDTOs);
    }




    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<HomeLocationResponseDTO>> getHomeLocationWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {

        return ResponseEntity.ok(
             
            homeLocationService.gethomeLocationsWithPagination(page, pageSize)

        );
    }




    @PutMapping("/{homelocationIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updateHomeLocationsInBatch(
        @Parameter(
            description = "Comma-separated list of homeLocation IDs to be updated",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> homelocationIds,
        @Parameter(
            description = "homeLocation object containing the fields to update",
            required = true
        )
        @Valid @RequestBody HomeLocationRequestDTO homeLocationRequestDTO
    ) {
        return ResponseEntity.ok(

            homeLocationService.updateHomeLocationsInBatch(homelocationIds, homeLocationRequestDTO)

        );
    }
}
