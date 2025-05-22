package com.trackswiftly.vehicle_service.dtos;


import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.vehicle_service.enums.CapacityType;
import com.trackswiftly.vehicle_service.enums.EngineType;
import com.trackswiftly.vehicle_service.enums.FuelType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


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


    // 🆕 Embedded capacity object
    @Valid
    @NotNull(groups = CreateValidationGroup.class, message = "Capacity is required")
    private CapacityRequest capacity;

    // 🆕 Enum to determine how planner should use the capacity
    @NotNull(groups = CreateValidationGroup.class, message = "Default capacity type is required")
    private CapacityType defaultCapacityType;
}