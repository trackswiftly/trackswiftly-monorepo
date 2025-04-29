package com.trackswiftly.client_service.dtos.validators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidLocationValidator.class)
public @interface ValidLocation {
    String message() default "Invalid location coordinates";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
