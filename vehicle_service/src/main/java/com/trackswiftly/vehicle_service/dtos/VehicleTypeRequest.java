package com.trackswiftly.vehicle_service.dtos;

import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class VehicleTypeRequest {
    
    @NotNull(groups = CreateValidationGroup.class, message = "Name is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class}, message = "Name must not be blank")
    private String name ;

    @NotBlank(groups = {CreateValidationGroup.class, UpdateValidationGroup.class}, message = "Name must not be blank")
    private String description ;
}
