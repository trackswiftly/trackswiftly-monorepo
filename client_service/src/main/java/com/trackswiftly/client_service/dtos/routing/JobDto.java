package com.trackswiftly.client_service.dtos.routing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.trackswiftly.client_service.dtos.validators.ValidLocation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull(message = "Job ID cannot be null")
    @Positive(message = "Job ID must be positive")
    private Integer id;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description; 

    @NotNull(message = "Location cannot be null")
    @Size(min = 2, max = 2 , message = "Location must contain exactly 2 elements [longitude, latitude]")
    @ValidLocation
    private List<Double> location;


    @JsonProperty("location_index")
    private List<@NotNull @PositiveOrZero Integer> locationIndex;

    @Min(value = 0, message = "Setup time must be non-negative")
    @Builder.Default
    private Integer setup = 0;

     @Min(value = 180, message = "Service time must be non-negative")
    @Builder.Default
    private Integer service = 0;


    private List<@Positive Integer> delivery;

    private List<@Positive Integer> pickup;

    private List<@PositiveOrZero Integer> skills;


    @Min(value = 0, message = "Priority must be between 0 and 100")
    @Max(value = 100, message = "Priority must be between 0 and 100")
    @Builder.Default
    private Integer priority = 0;


    @JsonProperty("time_windows")
    @Valid
    private List<TimeWindow> timeWindows;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeWindow {

        @NotNull(message = "Time window start cannot be null")
        @Min(value = 0, message = "Time window start must be non-negative")
        private Integer start;

        @NotNull(message = "Time window end cannot be null")
        @Min(value = 0, message = "Time window end must be non-negative")
        private Integer end;

        @AssertTrue(message = "Time window end must be greater than start")
        public boolean isValidTimeWindow() {
            return start == null || end == null || end > start;
        }
    }
    
}
