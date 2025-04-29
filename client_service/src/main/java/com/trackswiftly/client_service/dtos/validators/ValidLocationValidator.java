package com.trackswiftly.client_service.dtos.validators;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidLocationValidator implements ConstraintValidator<ValidLocation, List<Double>>{


    private static final double MIN_LONGITUDE = -180.0;
    private static final double MAX_LONGITUDE = 180.0;
    private static final double MIN_LATITUDE = -90.0;
    private static final double MAX_LATITUDE = 90.0;


    @Override
    public boolean isValid(List<Double> location, ConstraintValidatorContext context) {
        if (location == null) return true; // Assume optional, remove if required
        
        if (location.size() != 2) return false;
        
        Double longitude = location.get(0);
        Double latitude = location.get(1);
        
        return isValidLongitude(longitude) && isValidLatitude(latitude);
    }
    


    private boolean isValidLongitude(Double value) {
        return value != null && value >= MIN_LONGITUDE && value <= MAX_LONGITUDE;
    }

    private boolean isValidLatitude(Double value) {
        return value != null && value >= MIN_LATITUDE && value <= MAX_LATITUDE;
    }
}
