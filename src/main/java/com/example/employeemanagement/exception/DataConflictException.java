package com.example.employeemanagement.exception;

/**
 * Exception thrown when there's a conflict with existing data
 */
public class DataConflictException extends RuntimeException {
    
    public DataConflictException(String message) {
        super(message);
    }
    
    public DataConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
