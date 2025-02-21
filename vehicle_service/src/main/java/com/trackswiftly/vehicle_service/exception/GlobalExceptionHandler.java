package com.trackswiftly.vehicle_service.exception;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trackswiftly.vehicle_service.dtos.ErrorResponse;
import com.trackswiftly.vehicle_service.dtos.ErrorResponse.ErrorDetail;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice 
public class GlobalExceptionHandler {


    @ExceptionHandler(UnableToProccessIteamException.class)
    public ResponseEntity<ErrorResponse> handleUnableToProccessIteamException(UnableToProccessIteamException ex) {
        log.error("UnableToProccessIteamException: {}", ex.getMessage());

        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail();
        errorDetail.setField("general"); // Default field
        errorDetail.setMessage(ex.getMessage());

        // Create the error response
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Processing failed");
        errorResponse.setErrors(List.of(errorDetail));


        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Validation failed"); 
        List<ErrorDetail> validationErrors = ex.getConstraintViolations().stream()
        .map(violation -> {
            
            ErrorDetail errorDetail = new ErrorDetail();

            errorDetail.setField(extractSimpleFieldName(violation.getPropertyPath().toString()));
            errorDetail.setMessage(violation.getMessage());

            return errorDetail;
        })
        .toList();

        errorResponse.setErrors(validationErrors);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    

    private String extractSimpleFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
    
}
