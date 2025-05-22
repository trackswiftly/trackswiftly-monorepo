package com.trackswiftly.vehicle_service.enums;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Data;

@Embeddable
@Data
@Builder
public class Capacity {
    

    private Double maxWeightKg;
    private Double maxVolumeM3;
    private Integer maxBoxes;
    private Integer maxPallets;

}
