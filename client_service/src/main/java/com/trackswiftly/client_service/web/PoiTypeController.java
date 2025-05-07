package com.trackswiftly.client_service.web;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

import com.trackswiftly.client_service.dtos.PoiTypeRequest;
import com.trackswiftly.client_service.dtos.PoiTypeResponse;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.client_service.services.PoiTypeService;
import com.trackswiftly.utils.dtos.OperationResult;
import com.trackswiftly.utils.dtos.PageDTO;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/types")
@Slf4j
@Validated
public class PoiTypeController {


    private PoiTypeService poiTypeService ;


    @Autowired
    PoiTypeController(
        PoiTypeService poiTypeService
    ) {
        this.poiTypeService = poiTypeService ;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<PoiTypeResponse> createpoiTypes(
        @RequestBody @Valid List<PoiTypeRequest> poiTypeRequests
    ) {
        List<PoiTypeResponse> responses = poiTypeService.createEntities(poiTypeRequests);

        log.debug("poiTypes {}" , responses);
        
        return responses ;
    }



    @DeleteMapping("/{typeIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> deletePoiTypes(
        @Parameter(
            description = "Comma-separated list of PoiType IDs to be deleted",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> typeIds
    ) {
        OperationResult result = poiTypeService.deleteEntities(typeIds);
        return ResponseEntity.ok(result);
    }




    @GetMapping("/{typeIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<PoiTypeResponse>> findPoiType(
        @Parameter(
            description = "Comma-separated list of group IDs to be fetched",
            required = true,
            example = "1,2,3",
            schema = @Schema(type = "string")
        )
        @PathVariable List<Long> typeIds
    ) {
        List<PoiTypeResponse> poiTypeResponses = poiTypeService.findEntities(typeIds);

        return ResponseEntity.ok(poiTypeResponses);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<PoiTypeResponse>> getPoiTypeWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<PoiTypeResponse> poiTypeResponses = poiTypeService.pageEntities(page, pageSize);

        return ResponseEntity.ok(poiTypeResponses);
    }



    @PutMapping("/{typeIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updatePoiTypesInBatch(
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
        @Valid @RequestBody PoiTypeRequest poiTypeRequest
    ) {
        OperationResult result = poiTypeService.updateEntities(typeIds, poiTypeRequest);
        return ResponseEntity.ok(result);
    }
    


    @GetMapping("/search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<PoiTypeResponse>> search(
        @RequestParam String query
    ) {
        String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
        return ResponseEntity.ok(
            poiTypeService.search(decodedQuery)
        );
    }
}