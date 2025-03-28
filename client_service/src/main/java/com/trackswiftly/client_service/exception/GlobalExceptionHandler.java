package com.trackswiftly.client_service.exception;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trackswiftly.client_service.dtos.ErrorResponse;
import com.trackswiftly.client_service.dtos.ErrorResponse.ErrorDetail;
import com.trackswiftly.client_service.utils.ConstraintViolationExtractor;
import com.trackswiftly.utils.exception.UnableToProccessIteamException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice 
public class GlobalExceptionHandler {


    private ConstraintViolationExtractor constraintViolationExtractor;


    @Autowired
    GlobalExceptionHandler(ConstraintViolationExtractor constraintViolationExtractor) {
        this.constraintViolationExtractor = constraintViolationExtractor;
    }

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
    




    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("DataIntegrityViolationException: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage("Data validation failed");
        
        Throwable rootCause = constraintViolationExtractor.getRootCause(ex);
        String errorMsg = rootCause.getMessage();
        
        ErrorResponse.ErrorDetail errorDetail = new ErrorResponse.ErrorDetail();
        
        if (rootCause instanceof SQLException sqlexception) {
            String sqlState = sqlexception.getSQLState();
            
            switch (sqlState) {
                case "23505":
                    constraintViolationExtractor.extractUniqueConstraintViolation(errorDetail, errorMsg);
                    break;
                case "23503":
                    constraintViolationExtractor.extractForeignKeyViolation(errorDetail, errorMsg);
                    break;
                case "23502":
                    constraintViolationExtractor.extractNotNullViolation(errorDetail, errorMsg);
                    break;
                case "23514":
                    constraintViolationExtractor.extractCheckConstraintViolation(errorDetail, errorMsg);
                    break;
                default:
                    errorDetail.setField("database");
                    errorDetail.setMessage("Database constraint violation: " + sqlState);
                    break;
            }
        } else {
            errorDetail.setField("general");
            errorDetail.setMessage("Database operation failed: " + errorMsg);
        }
        
        errorResponse.setErrors(List.of(errorDetail));
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }




    private String extractSimpleFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
    
}
