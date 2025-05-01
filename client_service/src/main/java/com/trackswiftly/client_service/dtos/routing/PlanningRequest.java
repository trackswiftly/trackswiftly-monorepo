package com.trackswiftly.client_service.dtos.routing;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningRequest {

    private List<JobDto> jobs;
    private List<VehicleDto> vehicles;

    private Map<String, Object> options;
    
}
