package com.trackswiftly.vehicle_service.dtos;

import java.time.Instant;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupResponse{
    
    private Long id ;

    private String name ;

    private String description ;


    private Instant createdAt ;

    private Instant updatedAt ;
}
