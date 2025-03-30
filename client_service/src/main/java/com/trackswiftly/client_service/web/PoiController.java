package com.trackswiftly.client_service.web;

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


import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.PoiResponse;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.client_service.services.PoiService;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pois")
@Slf4j
@Validated
public class PoiController {


    private PoiService poiService ;

    @Autowired
    PoiController (
        PoiService poiService
    ) {
        this.poiService = poiService ;
    }
    


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<PoiResponse> createPois(
        @RequestBody @Valid List<PoiRequest> poiRequests
    ) {
        
        return poiService.createEntities(poiRequests) ;
    }


    @DeleteMapping("/{poiIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")   
    public ResponseEntity<OperationResult> deletePois(
        @Parameter(
            description = "Comma-separated list of Poi IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> poiIds
    ) {
        OperationResult result = poiService.deleteEntities(poiIds);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{poiIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<PoiResponse>> findPois(
        @Parameter(
            description = "Comma-separated list of Poi IDs to be fetched",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> poiIds
    ) {
        List<PoiResponse> pois = poiService.findEntities(poiIds);
        return ResponseEntity.ok(pois);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<PoiResponse>> getPoisWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<PoiResponse> pois = poiService.pageEntities(page, pageSize);

        return ResponseEntity.ok(pois);
    }


    @PutMapping("/{poiIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updatePoisInBatch(
        @Parameter(
            description = "Comma-separated list of Poi IDs to be updated",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> poiIds,
        @Parameter(
            description = "Poi object containing the fields to update",
            required = true
        )
        @Valid @RequestBody PoiRequest poi
    ) {
        OperationResult result = poiService.updateEntities(poiIds, poi);
        return ResponseEntity.ok(result);
    }
}