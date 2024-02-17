package com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AircraftEnvironmentLimitationsGlobalExceptionHandler {
	
	@ExceptionHandler(AircraftEnvironmentLimitationsCustomApplicationException.class)
    public ResponseEntity<String> handleCustomApplicationException(AircraftEnvironmentLimitationsCustomApplicationException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

}
