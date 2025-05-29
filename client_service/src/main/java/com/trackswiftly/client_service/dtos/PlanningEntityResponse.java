package com.trackswiftly.client_service.dtos;

import java.time.OffsetDateTime;

import com.trackswiftly.client_service.dtos.routing.PlanningResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PlanningEntityResponse {
    private Long id;
    private  PlanningResponse planningData;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
