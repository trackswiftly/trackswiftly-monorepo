package com.trackswiftly.client_service.dtos.routing;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanningResponse {


    private int code;
    private String error;
    private Summary summary;
    private List<Unassigned> unassigned;
    private List<Route> routes;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Summary {
        private Integer cost;
        private Integer routes;
        private Integer unassigned;
        private Integer setup;
        private Integer service;
        private Integer duration;
        private Integer waitingTime;
        private Integer priority;
        private List<Violation> violations;
        private List<Integer> amount; 
        private List<Integer> delivery;
        private List<Integer> pickup;
        private Integer distance;

        @JsonIgnore
        public Integer getTotalDelivery() {
            return delivery != null ? delivery.stream().mapToInt(Integer::intValue).sum() : 0;
        }
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Unassigned {
        private Integer id;
        private String type;
        private String description;
        private List<Double> location;
        
        @JsonProperty("location_index")
        private List<Integer> locationIndex;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Route {
        private Integer vehicle;
        private List<Step> steps;
        private Integer cost;
        private Integer setup;
        private Integer service;
        private Integer duration;
        private Integer waitingTime;
        private Integer priority;
        private List<Violation> violations;
        private List<Integer> amount;
        private List<Integer> delivery;
        private List<Integer> pickup;
        private String description;
        private String geometry;
        private Integer distance;

        @JsonProperty("waiting_time")
        public Integer getWaitingTime() { return waitingTime; }
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Step {
        private StepType type;
        private Integer arrival;
        private Integer duration;
        private Integer setup;
        private Integer service;
        private Integer waitingTime;
        private List<Violation> violations;
        private String description;
        private List<Double> location;
        
        @JsonProperty("location_index")
        private List<Integer> locationIndex;
        private Integer id;
        private Integer job;
        private List<Integer> load;
        private Integer distance;

        @JsonProperty("waiting_time")
        public Integer getWaitingTime() { return waitingTime; }

        public enum StepType {
            START("start"),
            JOB("job"),
            PICKUP("pickup"),
            DELIVERY("delivery"),
            BREAK("break"),
            END("end");

            private final String value;

            StepType(String value) {
                this.value = value;
            }

            @JsonValue
            public String getValue() {
                return value;
            }

            @JsonCreator
            public static StepType fromValue(String value) {
                for (StepType type : StepType.values()) {
                    if (type.value.equalsIgnoreCase(value)) {
                        return type;
                    }
                }
                throw new IllegalArgumentException("Unknown StepType: " + value);
            }
        }
    }



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Violation {
        private String cause;
        private Integer duration;
    }
    
}
