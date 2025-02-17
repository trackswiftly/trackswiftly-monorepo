package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.ModelRequest;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.entities.Model;



@Component
public class ModelMapper {
    



    public List<ModelResponse> toModelResponseList(List<Model> models) {


        return models.stream()
                        .map(this::toModelResponse)
                        .toList() ;
    }


    public List<Model> toModelList(List<ModelRequest> modelRequests) {
        return modelRequests.stream()
                .map(this::toModel)
                .toList();
    }







    public ModelResponse toModelResponse(Model model) {
        return ModelResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .make(model.getMake())
                .fuelType(model.getFuelType())
                .engineType(model.getEngineType())
                .maxPayloadWeight(model.getMaxPayloadWeight())
                .maxVolume(model.getMaxVolume())
                .year(model.getYear())
                .transmission(model.getTransmission())
                .updatedAt(model.getUpdatedAt())
                .createdAt(model.getCreatedAt())
                .build();
    }
    

    public Model toModel(ModelRequest modelRequest) {
        return Model.builder()
                .name(modelRequest.getName())
                .make(modelRequest.getMake())
                .fuelType(modelRequest.getFuelType())
                .engineType(modelRequest.getEngineType())
                .maxPayloadWeight(modelRequest.getMaxPayloadWeight())
                .maxVolume(modelRequest.getMaxVolume())
                .year(modelRequest.getYear())
                .transmission(modelRequest.getTransmission())
                .build();
    }
}
