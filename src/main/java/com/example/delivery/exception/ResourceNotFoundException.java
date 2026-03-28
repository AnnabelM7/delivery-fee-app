package com.example.delivery.exception;

public class ResourceNotFoundException extends RuntimeException {
    /**
     * Exception thrown when a requested resource is not found in the database.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}