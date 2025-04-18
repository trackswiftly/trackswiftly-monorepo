package com.trackswiftly.client_service.dtos;

import java.math.BigDecimal;
import java.util.Map;

import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PoiRequest {

    @NotNull(groups = CreateValidationGroup.class, message = "Name is required for create operation")
    @NotBlank(groups = {CreateValidationGroup.class}, message = "Name must not be blank")
    @Size(max = 255, groups = {CreateValidationGroup.class , UpdateValidationGroup.class} , message = "Name must not exceed 255 characters")
    private String name;

    @NotNull(groups = CreateValidationGroup.class, message = "Group ID is required for create operation")
    private Long groupId;

    @NotNull(groups = CreateValidationGroup.class, message = "Type ID is required for create operation")
    private Long typeId;

    @Size(max = 255, groups = {CreateValidationGroup.class , UpdateValidationGroup.class} , message = "Address must not exceed 255 characters" )
    private String address;

    @NotNull(groups = CreateValidationGroup.class, message = "Latitude is required for create operation")
    @Digits(
        integer = 2, 
        fraction = 8, 
        message = "Latitude must have up to 2 integer digits and 8 fractional digits" ,
        groups = {CreateValidationGroup.class , UpdateValidationGroup.class}
    )
    private BigDecimal latitude;

    @NotNull(groups = CreateValidationGroup.class, message = "Longitude is required for create operation")
    @Digits(
        integer = 3, 
        fraction = 8, 
        message = "Longitude must have up to 3 integer digits and 8 fractional digits" ,
        groups = {CreateValidationGroup.class , UpdateValidationGroup.class}
    )
    private BigDecimal longitude;

    private Map<String, Object> payload;
}
