package com.trackswiftly.vehicle_service.mappers;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.ModelRequest;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.enums.Capacity;



@Component
public class ModelMapper {
    



    public List<ModelResponse> toModelResponseList(List<Model> models) {
        
        if (models.isEmpty()) {
            return Collections.emptyList();
        }

        return models.stream()
                        .map(this::toModelResponse)
                        .toList() ;
    }


    public List<Model> toModelList(List<ModelRequest> modelRequests) {

        if (modelRequests.isEmpty()) {
            return Collections.emptyList();
        }
        return modelRequests.stream()
                .map(this::toModel)
                .toList();
    }







    public ModelResponse toModelResponse(Model model) {

        if (model == null) {
            return null;
        }
        return ModelResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .make(model.getMake())
                .fuelType(model.getFuelType())
                .engineType(model.getEngineType())
                .defaultCapacityType(model.getDefaultCapacityType())
                .capacity(model.getCapacity())
                .year(model.getYear())
                .transmission(model.getTransmission())
                .updatedAt(model.getUpdatedAt())
                .createdAt(model.getCreatedAt())
                .build();
    }
    

    public Model toModel(ModelRequest modelRequest) {

        if (modelRequest == null) {
            return null;
        }

        return Model.builder()
                .name(modelRequest.getName())
                .make(modelRequest.getMake())
                .fuelType(modelRequest.getFuelType())
                .engineType(modelRequest.getEngineType())
                .capacity(
                    Capacity.builder()
                        .maxWeightKg(modelRequest.getCapacity().getMaxWeightKg())
                        .maxVolumeM3(modelRequest.getCapacity().getMaxVolumeM3())
                        .maxBoxes(modelRequest.getCapacity().getMaxBoxes())
                        .maxPallets(modelRequest.getCapacity().getMaxPallets())
                        .build()
                )
                .defaultCapacityType(modelRequest.getDefaultCapacityType())
                .year(modelRequest.getYear())
                .transmission(modelRequest.getTransmission())
                .build();
    }
}
