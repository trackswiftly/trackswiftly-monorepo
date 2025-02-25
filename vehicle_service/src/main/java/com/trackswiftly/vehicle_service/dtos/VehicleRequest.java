package com.trackswiftly.vehicle_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleRequest {

    @NotNull(groups = CreateValidationGroup.class, message = "VIN is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class}, message = "VIN must not be blank")
    private String vin;

    @NotBlank(groups = {CreateValidationGroup.class}, message = "License plate must not be blank")
    private String licensePlate;

    @NotNull(groups = CreateValidationGroup.class, message = "Mileage is required for create operation")
    @PositiveOrZero(groups = {CreateValidationGroup.class, UpdateValidationGroup.class}, message = "Mileage must be a positive number or zero")
    private int mileage;

    private Instant purchaseDate;

    @NotNull(groups = CreateValidationGroup.class, message = "Vehicle type is required for create operation")
    private Long vehicleTypeId;

    @NotNull(groups = CreateValidationGroup.class, message = "Model is required for create operation")
    private Long modelId;

    private Long homeLocationId;

    @NotNull(groups = CreateValidationGroup.class, message = "Vehicle group is required for create operation")
    private Long vehicleGroupId;
}
