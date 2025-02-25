package com.trackswiftly.vehicle_service.dtos;


import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.enums.EngineType;
import com.trackswiftly.vehicle_service.enums.FuelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;


import java.util.Date;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModelRequest {

    @NotNull(groups = CreateValidationGroup.class, message = "Name is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class}, message = "Name must not be blank")
    private String name;

    @NotNull(groups = CreateValidationGroup.class, message = "Make is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class}, message = "Make must not be blank")
    private String make;

    // @NotNull(groups = CreateValidationGroup.class, message = "Year is required for create operation")
    private Date year;

    @NotNull(groups = CreateValidationGroup.class, message = "Engine type is required for create operation")
    private EngineType engineType;

    @NotNull(groups = CreateValidationGroup.class, message = "Fuel type is required for create operation")
    private FuelType fuelType;

    @Pattern(regexp = "^[A-Za-z]+$", message = "Transmission must only contain alphabetic characters", groups = { CreateValidationGroup.class, UpdateValidationGroup.class })
    private String transmission;

    @PositiveOrZero(groups = {CreateValidationGroup.class}, message = "Max payload weight must be a positive number or zero")
    private double maxPayloadWeight;

    @PositiveOrZero(groups = {CreateValidationGroup.class}, message = "Max volume must be a positive number or zero")
    private double maxVolume;
}