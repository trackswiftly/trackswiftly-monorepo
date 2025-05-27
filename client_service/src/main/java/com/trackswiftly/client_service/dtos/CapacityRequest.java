package com.trackswiftly.client_service.dtos;

import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacityRequest {

    @Positive(message = "Max payload weight must be positive" , groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Double maxWeightKg;

    @Positive(message = "Max volume must be positive" , groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Double maxVolumeM3;

    @Positive(message = "Max boxes must be positive" , groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Integer maxBoxes;

    @Positive(message = "Max pallets must be positive" , groups = {CreateValidationGroup.class, UpdateValidationGroup.class})
    private Integer maxPallets;
}
