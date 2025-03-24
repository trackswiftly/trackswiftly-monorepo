package com.trackswiftly.client_service.utils;

import com.trackswiftly.client_service.dtos.ErrorResponse.ErrorDetail;


import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class ConstraintViolationExtractor {
    



    public void extractUniqueConstraintViolation(ErrorDetail errorDetail, String errorMsg) {
        errorDetail.setField("unique_constraint");
        
        // Extract field name and value from detail message
        Pattern detailPattern = Pattern.compile("Detail: Key \\((.+)\\)=\\((.+)\\) already exists");
        Matcher matcher = detailPattern.matcher(errorMsg);
        
        if (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);
            
            // Clean up the field - handle composite keys
            field = field.replace(", ", " and ");
            // Clean up value - remove quotes and extra formatting
            value = value.replace("'", "").trim();
            
            errorDetail.setField(field);
            errorDetail.setMessage("A record with this " + field + " already exists: " + value);
        } else {
            errorDetail.setMessage("A duplicate record already exists");
        }
    }
    
    public void extractForeignKeyViolation(ErrorDetail errorDetail, String errorMsg) {
        errorDetail.setField("foreign_key");
        
        // Extract referenced table and constraint name
        Pattern detailPattern = Pattern.compile("Detail: Key \\((.+)\\)=\\((.+)\\) is not present in table \"(\\w+)\"");
        Matcher matcher = detailPattern.matcher(errorMsg);
        
        if (matcher.find()) {
            String field = matcher.group(1);
            String value = matcher.group(2);
            String table = matcher.group(3);
            
            errorDetail.setField(field);
            errorDetail.setMessage("Invalid reference: " + field + " with value " + value + 
                " does not exist in " + formatTableName(table));
        } else {
            errorDetail.setMessage("Invalid reference: the referenced record does not exist");
        }
    }
    
    public void extractNotNullViolation(ErrorDetail errorDetail, String errorMsg) {
        errorDetail.setField("not_null");
        
        // Extract column name
        Pattern detailPattern = Pattern.compile("column \"(\\w+)\" of relation \"(\\w+)\" violates not-null constraint");
        Matcher matcher = detailPattern.matcher(errorMsg);
        
        if (matcher.find()) {
            String column = matcher.group(1);
            
            errorDetail.setField(column);
            errorDetail.setMessage("The " + formatFieldName(column) + " field cannot be empty");
        } else {
            errorDetail.setMessage("A required field cannot be empty");
        }
    }
    
    public void extractCheckConstraintViolation(ErrorDetail errorDetail, String errorMsg) {
        errorDetail.setField("check_constraint");
        
        // Extract constraint name
        Pattern detailPattern = Pattern.compile("constraint \"(\\w+)\" of relation \"(\\w+)\"");
        Matcher matcher = detailPattern.matcher(errorMsg);
        
        if (matcher.find()) {
            String constraint = matcher.group(1);
            
            errorDetail.setField(constraint);
            errorDetail.setMessage("The provided value violates validation rules: " + 
                formatConstraintName(constraint));
        } else {
            errorDetail.setMessage("The provided value violates validation rules");
        }
    }
    
    public Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause != null) {
            return getRootCause(cause);
        }
        return throwable;
    }
    
    public String extractSimpleFieldName(String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        return parts[parts.length - 1];
    }
    
    public String formatTableName(String tableName) {
        // Convert snake_case to readable format
        return tableName.replace('_', ' ');
    }
    
    public String formatFieldName(String fieldName) {
        // Convert snake_case or camelCase to readable format
        return fieldName
            .replaceAll("([A-Z])", " $1")
            .replace("_", " ")
            .toLowerCase()
            .trim();
    }
    
    public String formatConstraintName(String constraintName) {
        // Try to extract meaningful information from constraint names like chk_age_positive
        return constraintName
            .replaceAll("^(uk|fk|pk|chk|ck)_", "")
            .replace("_", " ")
            .trim();
    }
}
