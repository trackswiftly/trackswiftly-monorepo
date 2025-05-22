package com.trackswiftly.vehicle_service.dtos;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacityRequest {

    @PositiveOrZero(message = "Max payload weight must be zero or positive")
    private Double maxWeightKg;

    @PositiveOrZero(message = "Max volume must be zero or positive")
    private Double maxVolumeM3;

    @PositiveOrZero(message = "Max boxes must be zero or positive")
    private Integer maxBoxes;

    @PositiveOrZero(message = "Max pallets must be zero or positive")
    private Integer maxPallets;
}

