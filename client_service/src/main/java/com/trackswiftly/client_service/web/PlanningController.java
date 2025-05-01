package com.trackswiftly.client_service.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/planning")
@Slf4j
@Validated
public class PlanningController {

    private final PlannerClient plannerClient;


    @Autowired
    public PlanningController(PlannerClient plannerClient) {
        this.plannerClient = plannerClient;
    }




    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_DISPATCHER')")
    @Validated(CreateValidationGroup.class)
    public PlanningResponse optimizePlan(
        @RequestBody @Valid PlanningRequest planningRequests
    ) {

        log.debug("Received planning request 🐛: {} ", planningRequests);

        PlanningResponse response = plannerClient.createOptimizationPlan(planningRequests);

        log.debug("Optimization plan response: {}", response);


        return response ;

    }
    
}
