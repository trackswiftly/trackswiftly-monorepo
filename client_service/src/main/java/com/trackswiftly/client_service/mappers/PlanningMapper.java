package com.trackswiftly.client_service.mappers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trackswiftly.client_service.entities.PlanningEntity;
import com.trackswiftly.client_service.dtos.PlanningEntityResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PlanningMapper {


    public List<PlanningEntityResponse> toPlanningEntityResponseList(List<PlanningEntity> planningEntities) {

        return  planningEntities.stream()
                    .map(this::toPlanningEntityResponse)
                    .toList() ;
        
        
    }


    public PlanningEntityResponse toPlanningEntityResponse(PlanningEntity planningEntity) {
        return PlanningEntityResponse.builder()
                .id(planningEntity.getId())
                .planningData(planningEntity.getPlanningData())
                .createdAt(planningEntity.getCreatedAt())
                .updatedAt(planningEntity.getUpdatedAt())
                .build();
    }
    
}
