package com.example.delivery.exception;

public class ForbiddenVehicleException extends RuntimeException {
    public ForbiddenVehicleException() {
        super("Usage of selected vehicle type is forbidden");
    }
}