package com.trackswiftly.client_service.dtos.routing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackswiftly.client_service.dtos.validators.ValidLocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {

    private Integer id;

    private String description;

    @Size(min = 2, max = 2 , message = "Location must contain exactly 2 elements [longitude, latitude]")
    @ValidLocation
    private List<Double> location;


    @JsonProperty("location_index")
    private List<Integer> locationIndex;

    @Builder.Default
    private Integer setup = 0;

    @Builder.Default
    private Integer service = 0;


    private List<Integer> delivery;

    private List<Integer> pickup;

    private List<Integer> skills;


    @Min(0)
    @Max(100)
    @Builder.Default
    private Integer priority = 0;


    @JsonProperty("time_windows")
    private List<TimeWindow> timeWindows;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeWindow {
        private Integer start;
        private Integer end;
    }
    
}
