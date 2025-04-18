package com.trackswiftly.client_service.tests.validation;


import  org.assertj.core.api.Assertions;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;


@Slf4j
class PoiRequestValidationTest {


    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



    private Set<ConstraintViolation<PoiRequest>> validate(PoiRequest request) {
        return validator.validate(request, CreateValidationGroup.class);
    }




    @Test
    void createValidationWithMissingRequiredFieldsReturnsViolations() {
        PoiRequest request = new PoiRequest();
        request.setName(null);
        request.setGroupId(null);
        request.setTypeId(null);
        
        Set<ConstraintViolation<PoiRequest>> violations = validate(request);
        
        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Name must not be blank",
                "Name is required for create operation" ,
                "Longitude is required for create operation",
                "Latitude is required for create operation" ,
                "Group ID is required for create operation",
                "Type ID is required for create operation"
            );
    }



    // Test coordinate precision
    @Test
    void validationWithInvalidLatitudeReturnsDigitsViolation() {
        PoiRequest request = PoiRequest.builder()
            .name("Test")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("12333333.45678901")) // 8 fractional digits
            .longitude(new BigDecimal("333333333333.456789")) // 8 fractional digits
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validate(request);
        
        log.info("🐛 Violations: {} ", violations);

        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Latitude must have up to 2 integer digits and 8 fractional digits",
                "Longitude must have up to 3 integer digits and 8 fractional digits" 
            );
    }


    @Test
    void updateValidationDoesNotRequireCreateFields() {
        PoiRequest request = new PoiRequest();
        request.setName(null); // Allowed for update
        
        Set<ConstraintViolation<PoiRequest>> violations = validator.validate(
            request, 
            UpdateValidationGroup.class
        );
        
        Assertions.assertThat(violations).isEmpty();
    }

    
    
}