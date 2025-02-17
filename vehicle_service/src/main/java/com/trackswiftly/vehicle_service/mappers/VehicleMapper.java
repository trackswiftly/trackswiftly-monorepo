package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;


import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.entities.Vehicle;
import com.trackswiftly.vehicle_service.entities.VehicleType;

@Component
public class VehicleMapper {
    

    public List<VehicleResponse> toVehicleResponseList(List<Vehicle> vehicles) {


        return vehicles.stream()
                        .map(this::toVehicleResponse)
                        .toList() ;
    }


    public List<Vehicle> toVehicleList(List<VehicleRequest> vehicleRequests) {
        return vehicleRequests.stream()
                .map(this::toVehicle)
                .toList();
    }



    public VehicleResponse toVehicleResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .id(vehicle.getId())
                .vin(vehicle.getVin())
                .licensePlate(vehicle.getLicensePlate())
                .mileage(vehicle.getMileage())
                .purchaseDate(vehicle.getPurchaseDate())
                .vehicleType(vehicle.getVehicleType().getId())
                .model(vehicle.getModel().getId())
                .vhicleGroup(vehicle.getVehicleGroup().getId())
                .updatedAt(vehicle.getUpdatedAt())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }
    

    public Vehicle toVehicle(VehicleRequest vehicleRequest) {
        return Vehicle.builder()
                .vin(vehicleRequest.getVin())
                .licensePlate(vehicleRequest.getLicensePlate())
                .mileage(vehicleRequest.getMileage())
                .purchaseDate(vehicleRequest.getPurchaseDate())
                .vehicleType(
                    VehicleType.builder()
                                .id(vehicleRequest.getVehicleTypeId())
                                .build()   
                )
                .model(
                    Model.builder()
                        .id(vehicleRequest.getModelId())
                        .build()
                )
                .vehicleGroup(
                    Group.builder()
                        .id(vehicleRequest.getVehicleGroupId())
                        .build() 
                )
                .build();
    }
}
