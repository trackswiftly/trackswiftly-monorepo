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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.trackswiftly.client_service.dtos.CapacityRequest;
import com.trackswiftly.client_service.dtos.PoiRequest;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;
import com.trackswiftly.client_service.dtos.interfaces.UpdateValidationGroup;
import com.trackswiftly.utils.enums.CapacityType;


@Slf4j
class PoiRequestValidationTest {


    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



     private Set<ConstraintViolation<PoiRequest>> validateForCreate(PoiRequest request) {
        return validator.validate(request, CreateValidationGroup.class);
    }

    private Set<ConstraintViolation<PoiRequest>> validateForUpdate(PoiRequest request) {
        return validator.validate(request, UpdateValidationGroup.class);
    }




    @Test
    @DisplayName("Create validation with missing required fields should return violations")
    void createValidationWithMissingRequiredFieldsReturnsViolations() {
        PoiRequest request = PoiRequest.builder().build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        log.debug("Validation violations: {}", violations.size());
        
        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Name is required for create operation",
                "Name must not be blank",
                "Group ID is required for create operation",
                "Type ID is required for create operation",
                "Latitude is required for create operation",
                "Longitude is required for create operation",
                "Capacity is required",
                "Default capacity type is required"
            );
    }

    @Test
    @DisplayName("Create validation with valid required fields should pass")
    void createValidationWithValidRequiredFieldsPasses() {
        PoiRequest request = PoiRequest.builder()
            .name("Test POI")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("45.12345678"))
            .longitude(new BigDecimal("123.12345678"))
            .capacity(CapacityRequest.builder()
                .maxWeightKg(1000.0)
                .build())
            .defaultCapacityType(CapacityType.WEIGHT) // Assuming this enum exists
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        Assertions.assertThat(violations).isEmpty();
    }


     @Test
    @DisplayName("Validation with invalid coordinate precision should return digits violations")
    void validationWithInvalidCoordinatePrecisionReturnsDigitsViolation() {
        PoiRequest request = PoiRequest.builder()
            .name("Test POI")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("123.45678901")) // Too many integer digits (3 instead of max 2)
            .longitude(new BigDecimal("1234.45678901")) // Too many integer digits (4 instead of max 3)
            .capacity(CapacityRequest.builder()
                .maxWeightKg(1000.0)
                .build())
            .defaultCapacityType(CapacityType.WEIGHT)
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        log.debug("Coordinate precision violations: {}", violations);

        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Latitude must have up to 2 integer digits and 8 fractional digits",
                "Longitude must have up to 3 integer digits and 8 fractional digits"
            );
    }


    @Test
    @DisplayName("Update validation should not require create-only fields")
    void updateValidationDoesNotRequireCreateFields() {
        PoiRequest request = PoiRequest.builder()
            .name("Updated Name") // Only setting name for update
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForUpdate(request);
        
        Assertions.assertThat(violations).isEmpty();
    }


     @Test
    @DisplayName("Update validation should still validate field constraints when present")
    void updateValidationValidatesFieldConstraintsWhenPresent() {
        PoiRequest request = PoiRequest.builder()
            .name("A".repeat(256)) // Exceeds max 255 characters
            .address("B".repeat(256)) // Exceeds max 255 characters
            .latitude(new BigDecimal("123.45678901")) // Invalid precision
            .longitude(new BigDecimal("1234.45678901")) // Invalid precision
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForUpdate(request);
        
        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Name must not exceed 255 characters",
                "Address must not exceed 255 characters",
                "Latitude must have up to 2 integer digits and 8 fractional digits",
                "Longitude must have up to 3 integer digits and 8 fractional digits"
            );
    }

    

    @Test
    @DisplayName("Capacity validation should work correctly")
    void capacityValidationWorksCorrectly() {
        CapacityRequest invalidCapacity = CapacityRequest.builder()
            .maxWeightKg(-100.0) // Negative value
            .maxVolumeM3(-50.0) // Negative value
            .maxBoxes(-10) // Negative value
            .maxPallets(-5) // Negative value
            .build();

        // First, let's test the CapacityRequest directly
        Set<ConstraintViolation<CapacityRequest>> capacityViolations = 
            validator.validate(invalidCapacity, CreateValidationGroup.class);
        
        log.info("🐛 Direct capacity violations: {}", capacityViolations.size());
        capacityViolations.forEach(violation -> 
            log.info("🐛 Direct capacity violation: {}", violation.getMessage())
        );

        PoiRequest request = PoiRequest.builder()
            .name("Test POI")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("45.12345678"))
            .longitude(new BigDecimal("123.12345678"))
            .capacity(invalidCapacity)
            .defaultCapacityType(CapacityType.WEIGHT)
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        // Debug: Let's see what violations we actually get
        log.info("🐛 Actual violations found: {}", violations.size());
        violations.forEach(violation -> 
            log.info("🐛 Violation: {} on property: {}", 
                violation.getMessage(), 
                violation.getPropertyPath())
        );
        
        // Let's also try validating without groups to see if @Valid works at all
        Set<ConstraintViolation<PoiRequest>> defaultViolations = validator.validate(request);
        log.info("🐛 Default group violations: {}", defaultViolations.size());
        defaultViolations.forEach(violation -> 
            log.info("🐛 Default violation: {} on property: {}", 
                violation.getMessage(), 
                violation.getPropertyPath())
        );
        
        Assertions.assertThat(violations).extracting("message")
            .containsExactlyInAnyOrder(
                "Max payload weight must be positive",
                "Max volume must be positive",
                "Max boxes must be positive",
                "Max pallets must be positive"
            );
    }


    @Test
    @DisplayName("Valid capacity should pass validation")
    void validCapacityPassesValidation() {
        CapacityRequest validCapacity = CapacityRequest.builder()
            .maxWeightKg(1000.0)
            .maxVolumeM3(50.0)
            .maxBoxes(100)
            .maxPallets(20)
            .build();

        PoiRequest request = PoiRequest.builder()
            .name("Test POI")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("45.12345678"))
            .longitude(new BigDecimal("123.12345678"))
            .capacity(validCapacity)
            .defaultCapacityType(CapacityType.WEIGHT)
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        Assertions.assertThat(violations).isEmpty();
    }


    @Test
    @DisplayName("Null capacity values should be allowed")
    void nullCapacityValuesAreAllowed() {
        CapacityRequest capacityWithNulls = CapacityRequest.builder()
            .maxWeightKg(null)
            .maxVolumeM3(null)
            .maxBoxes(null)
            .maxPallets(null)
            .build();

        PoiRequest request = PoiRequest.builder()
            .name("Test POI")
            .groupId(1L)
            .typeId(1L)
            .latitude(new BigDecimal("45.12345678"))
            .longitude(new BigDecimal("123.12345678"))
            .capacity(capacityWithNulls)
            .defaultCapacityType(CapacityType.WEIGHT)
            .build();
        
        Set<ConstraintViolation<PoiRequest>> violations = validateForCreate(request);
        
        Assertions.assertThat(violations).isEmpty();
    }

    
}