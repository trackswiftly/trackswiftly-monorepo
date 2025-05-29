package com.trackswiftly.client_service.services;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trackswiftly.client_service.clients.PlannerClient;
import com.trackswiftly.client_service.dao.repositories.PlanningRepo;
import com.trackswiftly.client_service.dtos.routing.PlanningRequest;
import com.trackswiftly.client_service.dtos.routing.PlanningResponse;
import com.trackswiftly.client_service.entities.PlanningEntity;
import com.trackswiftly.client_service.dtos.PlanningEntityResponse;
import com.trackswiftly.client_service.mappers.PlanningMapper;

import jakarta.transaction.Transactional;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Builder
@Service
@Transactional
public class PlanningService {

    private final PlanningRepo planningRepo;

    private final PlannerClient plannerClient;

    private final PlanningMapper planningMapper ;


    @Autowired
    PlanningService(
        PlanningRepo planningRepo ,
        PlannerClient plannerClient ,
        PlanningMapper planningMapper
    ) {
        this.planningRepo = planningRepo;
        this.plannerClient = plannerClient;
        this.planningMapper = planningMapper;
    }



    public List<PlanningEntityResponse> createPlanningEnitities(List<PlanningRequest> planningRequests) {
        log.info("Creating groups in batch...");

        PlanningResponse response = plannerClient.createOptimizationPlan(planningRequests.get(0));
        
        return planningMapper.toPlanningEntityResponseList(
                    planningRepo.insertInBatch(
                        List.of(
                            PlanningEntity.builder()
                                .planningData(response)
                                .build()
                        )
                    )
                );
    }



    public List<PlanningEntityResponse> findByTimeRange(long from, long to) {
        
        OffsetDateTime fromTime = Instant.ofEpochSecond(from).atOffset(ZoneOffset.UTC);
        OffsetDateTime toTime = Instant.ofEpochSecond(to).atOffset(ZoneOffset.UTC);


        return planningMapper.toPlanningEntityResponseList(
                    planningRepo.findByTimeRange(fromTime, toTime)
        );
        
    }

}
