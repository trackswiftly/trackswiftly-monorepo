package com.trackswiftly.vehicle_service.dtos;

import java.time.Instant;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupResponse {
    
    private Long id ;

    private String name ;

    private String description ;


    private Instant createdAt ;

    private Instant updatedAt ;
}
