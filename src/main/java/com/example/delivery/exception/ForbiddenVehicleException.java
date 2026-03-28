package com.example.delivery.exception;

public class ForbiddenVehicleException extends RuntimeException {
    /**
     * Exception thrown when the selected vehicle type is forbidden
     * due to current weather conditions.
     */
    public ForbiddenVehicleException() {
        super("Usage of selected vehicle type is forbidden");
    }
}