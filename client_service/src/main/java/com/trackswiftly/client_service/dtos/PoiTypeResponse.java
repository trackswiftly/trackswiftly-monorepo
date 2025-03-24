package com.trackswiftly.client_service.dtos;

import java.time.Instant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PoiTypeResponse {
    
    private Long id ;

    private String name ;

    private String description ;


    private Instant createdAt ;

    private Instant updatedAt ;
}
