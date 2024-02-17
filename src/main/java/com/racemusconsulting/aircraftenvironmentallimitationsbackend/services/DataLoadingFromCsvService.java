package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.AircraftModelEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.AircraftModelRepository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.TemperatureDeviationRepository;

import jakarta.annotation.PostConstruct;

@Component
public class DataLoadingFromCsvService {

	private static final Logger log = LoggerFactory.getLogger(DataLoadingFromCsvService.class);

	@Autowired
	private AircraftModelRepository aircraftModelRepository;

	@Autowired
	private TemperatureDeviationRepository temperatureDeviationRepository;

	@PostConstruct
	@Transactional
	public void loadCsvData() throws IOException {
	}

	private void processLine(String line) {
		String[] values = line.split(";");
		String model = values[0].trim();
		AircraftModelEntity aircraftModel = findOrCreateAircraftModel(model);

		if (values.length == 5) {
			processAndSaveDeviations(aircraftModel, values[1], "MAX", "IN_FLIGHT");
			processAndSaveDeviations(aircraftModel, values[2], "MIN", "IN_FLIGHT");
			processAndSaveDeviations(aircraftModel, values[3], "MAX", "TOLD");
			processAndSaveDeviations(aircraftModel, values[4], "MIN", "TOLD");
		}
	}
	
	private AircraftModelEntity findOrCreateAircraftModel(String model) {
		return null;
	}
	
	private void processAndSaveDeviations(AircraftModelEntity aircraftModel, String deviationData, String type,
			String phase) {

	}
}