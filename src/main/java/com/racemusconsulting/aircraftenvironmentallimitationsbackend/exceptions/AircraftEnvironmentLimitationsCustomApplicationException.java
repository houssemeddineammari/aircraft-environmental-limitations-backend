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

	public static AircraftEnvironmentLimitationsCustomApplicationException modelNotFound(String message) {
		return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.BAD_REQUEST);
	}

	public static AircraftEnvironmentLimitationsCustomApplicationException altitudeNotFound(String message) {
		return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.BAD_REQUEST);
	}

	public static AircraftEnvironmentLimitationsCustomApplicationException phaseNotFound(String message) {
		return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.BAD_REQUEST);
	}

	public static AircraftEnvironmentLimitationsCustomApplicationException temperatureDeviationNotFound(
			String message) {
		return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.NOT_FOUND);
	}

	public static AircraftEnvironmentLimitationsCustomApplicationException temperatureInterpolationException(
			String message) {
		return new AircraftEnvironmentLimitationsCustomApplicationException(message, HttpStatus.BAD_REQUEST);
	}
}
