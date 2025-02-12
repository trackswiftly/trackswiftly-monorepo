package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.VehicleTypeRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleTypeResponse;
import com.trackswiftly.vehicle_service.entities.VehicleType;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class VehicleTypeMapper {



    public List<VehicleTypeResponse> toVehicleTypeResponseList(List<VehicleType> vehicleTypes) {


        List<VehicleTypeResponse> vehicleTypeResponses = vehicleTypes.stream()
                                            .map(this::toVehicleTypeResponse)
                                            .toList() ;
        
        log.debug("vehicleTypeResponses :{} " , vehicleTypeResponses);
        return vehicleTypeResponses;
    }


    public List<VehicleType> toVehicleTypeList(List<VehicleTypeRequest> vehicleTypeRequests) {
        return vehicleTypeRequests.stream()
                .map(this::toVehicleType)
                .toList();
    }
    



    public VehicleTypeResponse toVehicleTypeResponse(VehicleType vehicleType) {
        return VehicleTypeResponse.builder()
                .id(vehicleType.getId())
                .name(vehicleType.getName())
                .description(vehicleType.getDescription())
                .updatedAt(vehicleType.getUpdatedAt())
                .createdAt(vehicleType.getCreatedAt())
                .build();
    }
    

    public VehicleType toVehicleType(VehicleTypeRequest vehicleTypeRequest) {
        return VehicleType.builder()
                .name(vehicleTypeRequest.getName())
                .description(vehicleTypeRequest.getDescription())
                .build();
    }
}
