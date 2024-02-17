package com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions;

import org.springframework.http.HttpStatus;

public class AircraftEnvironmentLimitationsCustomApplicationException extends RuntimeException {
    private final HttpStatus status;

    public AircraftEnvironmentLimitationsCustomApplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
    
    public static AircraftEnvironmentLimitationsCustomApplicationException csvFileReadError(String message) {
        return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static AircraftEnvironmentLimitationsCustomApplicationException csvParsingError(String message) {
        return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.BAD_REQUEST);
    }
}