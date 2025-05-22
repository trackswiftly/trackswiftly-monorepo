package com.trackswiftly.vehicle_service.dtos;

import com.trackswiftly.vehicle_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.vehicle_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HomeLocationRequestDTO {

    @NotBlank(groups = {CreateValidationGroup.class})
    @Size(max = 255, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private String name;

    @NotNull(groups = {CreateValidationGroup.class})
    @DecimalMin(value = "-90.0", inclusive = true, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @DecimalMax(value = "90.0", inclusive = true, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Double latitude;

    @NotNull(groups = {CreateValidationGroup.class})
    @DecimalMin(value = "-180.0", inclusive = true, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    @DecimalMax(value = "180.0", inclusive = true, groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Double longitude;

    @NotNull(groups = {CreateValidationGroup.class})
    private String address ;
}
