package com.trackswiftly.vehicle_service.dtos;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class HomeLocationResponseDTO {
    
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String address ;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
