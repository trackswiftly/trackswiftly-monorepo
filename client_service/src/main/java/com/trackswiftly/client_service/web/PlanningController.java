package com.trackswiftly.client_service.web;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trackswiftly.client_service.clients.PlannerClient;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.routing.PlanningRequest;
import com.trackswiftly.client_service.dtos.routing.PlanningResponse;
import com.trackswiftly.client_service.entities.PlanningEnitity;
import com.trackswiftly.client_service.services.PlanningService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/planning")
@Slf4j
@Validated
public class PlanningController {

    // private final PlannerClient plannerClient;
    private final PlanningService planningService;


    @Autowired
    public PlanningController(PlanningService planningService) {
        this.planningService = planningService;
    }




    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public PlanningEnitity optimizePlan(
        @RequestBody @Valid PlanningRequest planningRequests
    ) {

        log.debug("Received planning request 🐛: {} ", planningRequests);

        // PlanningResponse response = plannerClient.createOptimizationPlan(planningRequests);

        List<PlanningEnitity> responses = planningService.createPlanningEnitities(
            List.of(planningRequests)
        );

        log.debug("Optimization plan response: {}", responses.get(0));


        return responses.get(0) ;

    }
}
