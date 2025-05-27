package com.trackswiftly.vehicle_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.vehicle_service.dtos.GroupResponse;
import com.trackswiftly.vehicle_service.dtos.HomeLocationResponseDTO;
import com.trackswiftly.vehicle_service.dtos.ModelResponse;
import com.trackswiftly.vehicle_service.dtos.VehicleRequest;
import com.trackswiftly.vehicle_service.dtos.VehicleResponse;
import com.trackswiftly.vehicle_service.entities.Group;
import com.trackswiftly.vehicle_service.entities.HomeLocation;
import com.trackswiftly.vehicle_service.entities.Model;
import com.trackswiftly.vehicle_service.entities.Vehicle;

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

    public List<Vehicle> toVehicleList(List<VehicleRequest> vehicleRequests , Group defaultGroup) {
        return vehicleRequests.stream()
            .map(request -> toVehicle(request, defaultGroup))
            .toList();
    }



    public VehicleResponse toVehicleResponse(Vehicle vehicle) {
        VehicleResponse.VehicleResponseBuilder responseBuilder = VehicleResponse.builder()
            .id(vehicle.getId())
            .vin(vehicle.getVin())
            .licensePlate(vehicle.getLicensePlate())
            .mileage(vehicle.getMileage())
            .purchaseDate(vehicle.getPurchaseDate())
            .vehicleType(vehicle.getVehicleType())
            .model(
                ModelResponse.builder()
                .id(vehicle.getModel().getId())
                .name(vehicle.getModel().getName())
                .make(vehicle.getModel().getMake())
                .capacity(vehicle.getModel().getCapacity())
                .defaultCapacityType(vehicle.getModel().getDefaultCapacityType())
                .fuelType(vehicle.getModel().getFuelType())
                .engineType(vehicle.getModel().getEngineType())
                .year(vehicle.getModel().getYear())
                .createdAt(vehicle.getModel().getCreatedAt())
                .build()
            )
            .vhicleGroup(
                GroupResponse.builder()
                .id(vehicle.getVehicleGroup().getId())
                .name(vehicle.getVehicleGroup().getName())
                .build()
            );

        HomeLocation homeLocation = vehicle.getHomeLocation();
        
        if (homeLocation != null) {
            responseBuilder.homeLocation(
                HomeLocationResponseDTO.builder()
                .id(homeLocation.getId())
                .name(homeLocation.getName())
                .latitude(homeLocation.getLatitude())
                .longitude(homeLocation.getLongitude())
                .build()
            );
        }

        return responseBuilder
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
                .vehicleType(vehicleRequest.getVehicleType())
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
                .homeLocation(
                    vehicleRequest.getHomeLocationId() != null ?
                    HomeLocation.builder()
                        .id(vehicleRequest.getHomeLocationId())
                        .build()
                    : null
                )
                .build();
    }



    public Vehicle toVehicle(VehicleRequest vehicleRequest , Group defaultGroup) {
        return Vehicle.builder()
                .vin(vehicleRequest.getVin())
                .licensePlate(vehicleRequest.getLicensePlate())
                .mileage(vehicleRequest.getMileage())
                .purchaseDate(vehicleRequest.getPurchaseDate())
                .vehicleType(vehicleRequest.getVehicleType())
                .model(
                    Model.builder()
                        .id(vehicleRequest.getModelId())
                        .build()
                )
                .vehicleGroup(
                    vehicleRequest.getVehicleGroupId() != null ?

                    Group.builder()
                        .id(vehicleRequest.getVehicleGroupId())
                        .build()
                    : defaultGroup
    
                )
                .build();
    }
}
