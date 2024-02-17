package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;

public interface TemperatureDeviationService {
	TemperatureDeviationResponseDTO getTemperatureDeviation(String model, Double altitude, String phase);

}
