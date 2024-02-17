package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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

	}
}