package com.trackswiftly.vehicle_service.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.vehicle_service.dao.repositories.ModelRepo;
import com.trackswiftly.vehicle_service.dtos.ModelRequest;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.dtos.OperationResult;
import com.trackswiftly.vehicle_service.dtos.PageDTO;

import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.mappers.ModelMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ModelService {
    
    private ModelRepo modelRepo ;

    private ModelMapper modelMapper ;


    @Autowired
    ModelService(
        ModelRepo modelRepo ,
        ModelMapper modelMapper
    ) {
        this.modelRepo = modelRepo ;

        this.modelMapper = modelMapper ;
    }



    public List<ModelResponse> createModels(List<ModelRequest> modelRequests) {
        

        List<Model> models = modelRepo.insertInBatch(modelMapper.toModelList(modelRequests) ) ;


        return modelMapper.toModelResponseList(models);
    }



    public OperationResult deleteModels(List<Long> modelIds) {

        if (modelIds == null || modelIds.isEmpty()) {
            return OperationResult.of(0);
        }

        int count = modelRepo.deleteByIds(modelIds); 

        return OperationResult.of(count) ;
    }


    public List<ModelResponse> findModels(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }

        List<Model> models = modelRepo.findByIds(ids);

        return modelMapper.toModelResponseList(models);
    }



    public PageDTO<ModelResponse> getModelsWithPagination(int page, int pageSize) {

        if (page < 0 || pageSize <= 0) {
            throw new IllegalArgumentException("Page and pageSize must be positive values");
        }


        List<ModelResponse> content = modelMapper.toModelResponseList( // mappe Models to Modelsrespones
            modelRepo.findWithPagination(page, pageSize) // get grooup data
        ) ;


        long totalElements = modelRepo.count();

        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        return new PageDTO<>(content, page, pageSize, totalElements, totalPages);
    }


    public OperationResult updateModelsInBatch(List<Long> modelIds, ModelRequest model) {
        
        if (modelIds == null || modelIds.isEmpty()) {
            throw new IllegalArgumentException("model IDs list cannot be null or empty");
        }

        if (model == null) {
            throw new IllegalArgumentException("model object cannot be null");
        }

        int count = modelRepo.updateInBatch(modelIds, modelMapper.toModel(model)) ;

        return OperationResult.of(count);
    }
}
