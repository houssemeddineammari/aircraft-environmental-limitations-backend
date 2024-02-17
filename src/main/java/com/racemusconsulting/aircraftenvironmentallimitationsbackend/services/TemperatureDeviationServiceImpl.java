package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import org.springframework.stereotype.Service;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions.AircraftEnvironmentLimitationsCustomApplicationException;

@Service
public class TemperatureDeviationServiceImpl implements TemperatureDeviationService {

	@Override
	public TemperatureDeviationResponseDTO getTemperatureDeviation(String model, Double altitude, String phase) {
		validateInputParameters(model, altitude, phase);
		return null;
	}

	private void validateInputParameters(String model, Double altitude, String phase) {
		if (model == null || model.trim().isEmpty()) {
			throw AircraftEnvironmentLimitationsCustomApplicationException
					.modelNotFound("Le modèle d'avion ne peut pas être null ou vide.");
		}
		if (altitude == null) {
			throw AircraftEnvironmentLimitationsCustomApplicationException
					.altitudeNotFound("L'altitude ne peut pas être null.");
		}
		if (phase == null || phase.trim().isEmpty()) {
			throw AircraftEnvironmentLimitationsCustomApplicationException
					.phaseNotFound("La phase aéroportée ne peut pas être null ou vide.");
		}
	}
}
