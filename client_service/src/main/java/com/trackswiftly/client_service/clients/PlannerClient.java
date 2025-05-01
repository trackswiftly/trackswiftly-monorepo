package com.trackswiftly.client_service.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.trackswiftly.client_service.dtos.routing.PlanningRequest;
import com.trackswiftly.client_service.dtos.routing.PlanningResponse;

@FeignClient(name = "plannerClient", url = "https://planner.geomax.ma")
public interface PlannerClient {

    @PostMapping
    PlanningResponse createOptimizationPlan(@RequestBody PlanningRequest request);

}
