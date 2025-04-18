package com.trackswiftly.client_service.tests.validation;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.trackswiftly.client_service.dtos.GroupRequest;
import com.trackswiftly.client_service.dtos.interfaces.CreateValidationGroup;

class GroupRequestValidationTest {


    private static Validator validator;


    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }



    // Helper method for valid create request
    private GroupRequest validCreateRequest() {
        return GroupRequest.builder()
            .name("東京Tokyo")
            .description("Valid description with spaces, numbers 123, and punctuation!")
            .build();
    }



    // CREATE VALIDATION GROUP TESTS
    @Test
    void createValidationWithValidRequestNoViolations() {
        GroupRequest request = validCreateRequest();
        Set<ConstraintViolation<GroupRequest>> violations = 
            validator.validate(request, CreateValidationGroup.class);
        Assertions.assertThat(violations).isEmpty();
    }


    @Test
    void createValidationWithNullNameReturnsViolations() {
        GroupRequest request = GroupRequest.builder()
                                .name(null)
                                .description("Valid description")
                                .build();
        
        Set<ConstraintViolation<GroupRequest>> violations = 
            validator.validate(request, CreateValidationGroup.class);
        
        Assertions.assertThat(violations)
            .extracting("message")
            .containsExactlyInAnyOrder(
                "Name is required for create operation",
                "Name must not be blank"
            );
    }
    
}
