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
import com.trackswiftly.client_service.entities.PlanningEnitity;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@Service
@Transactional
public class PlanningService {

    private final PlanningRepo planningRepo;

    private final PlannerClient plannerClient;


    @Autowired
    PlanningService(
        PlanningRepo planningRepo ,
        PlannerClient plannerClient
    ) {
        this.planningRepo = planningRepo;
        this.plannerClient = plannerClient;
    }



    public List<PlanningEnitity> createPlanningEnitities(List<PlanningRequest> planningRequests) {
        log.info("Creating groups in batch...");

        PlanningResponse response = plannerClient.createOptimizationPlan(planningRequests.get(0));
        
        return  planningRepo.insertInBatch(
                    List.of(
                        PlanningEnitity.builder()
                            .planningData(response)
                            .build()
                    )
                ) ;
    }



    public List<PlanningEnitity> findByIdsAndTimeRange(List<Long> ids , long from, long to) {
        
        OffsetDateTime fromTime = Instant.ofEpochSecond(from).atOffset(ZoneOffset.UTC);
        OffsetDateTime toTime = Instant.ofEpochSecond(to).atOffset(ZoneOffset.UTC);

       return planningRepo.findByIdsAndTimeRange(ids, fromTime, toTime);
        
    }

}
