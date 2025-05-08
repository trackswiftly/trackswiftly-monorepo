package com.trackswiftly.vehicle_service.dtos;

import java.time.Instant;

import com.trackswiftly.vehicle_service.enums.VehicleTypeEnum;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VehicleResponse {
    

    private Long id ;


    private String vin ;


    private String licensePlate ;


    private int mileage ;


    private Instant purchaseDate ;


    private VehicleTypeEnum vehicleType ;

    private ModelResponse model ;


    private HomeLocationResponseDTO homeLocation ;

    private GroupResponse vhicleGroup ;

    private Instant createdAt ;

    private Instant updatedAt ;
}
