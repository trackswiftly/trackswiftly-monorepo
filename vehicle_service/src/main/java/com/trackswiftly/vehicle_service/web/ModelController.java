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


import com.trackswiftly.vehicle_service.dtos.ModelRequest;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;
import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.services.ModelService;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/models")
@Slf4j
public class ModelController {
    

    private ModelService modelService ;

    @Autowired
    ModelController (
        ModelService modelService
    ) {
        this.modelService = modelService ;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public List<ModelResponse> createModels(
        @RequestBody @Valid List<ModelRequest> modelRequests
    ) {
        return modelService.createModels(modelRequests);
    }


    @DeleteMapping("/{modelIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")   
    public ResponseEntity<OperationResult> deleteModels(
        @PathVariable List<Long> modelIds
    ) {
        OperationResult result = modelService.deleteModels(modelIds);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{modelIds}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<List<ModelResponse>> findModels(

        @PathVariable List<Long> modelIds
    ) {
        List<ModelResponse> modelResponses = modelService.findModels(modelIds);
        return ResponseEntity.ok(modelResponses);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<PageDTO<ModelResponse>> getModelsWithPagination(
        @RequestParam int page,
        @RequestParam int pageSize
    ) {
        PageDTO<ModelResponse> models = modelService.getModelsWithPagination(page, pageSize);

        return ResponseEntity.ok(models);
    }


    @PutMapping("/{modelIds}")
    @Validated(UpdateValidationGroup.class)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    public ResponseEntity<OperationResult> updategroupsInBatch(
        
        @PathVariable List<Long> modelIds,

        @Valid @RequestBody ModelRequest modelRequest
    ) {
        OperationResult result = modelService.updateModelsInBatch(modelIds, modelRequest) ;
        return ResponseEntity.ok(result);
    }
}
