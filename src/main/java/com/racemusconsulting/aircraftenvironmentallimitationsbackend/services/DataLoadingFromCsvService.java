package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.AircraftModelEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions.AircraftEnvironmentLimitationsCustomApplicationException;
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
		ClassPathResource resource = new ClassPathResource("temperature_deviation.csv");

		try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
			br.readLine();
			String line;
			while ((line = br.readLine()) != null) {
				processLine(line);
			}
		} catch (IOException e) {
			throw AircraftEnvironmentLimitationsCustomApplicationException
					.csvFileReadError("Error reading CSV file: " + e.getMessage());
		}
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
		return aircraftModelRepository.findByModel(model).orElseGet(() -> {
			AircraftModelEntity newModel = new AircraftModelEntity();
			newModel.setModel(model);
			return aircraftModelRepository.save(newModel);
		});
	}

	private void processAndSaveDeviations(AircraftModelEntity aircraftModel, String deviationData, String type,
			String phase) {
		Arrays.stream(deviationData.split("\\|")).map(part -> part.split(":")).filter(parts -> parts.length == 2)
				.forEach(parts -> {
					try {
						Double altitude = Double.valueOf(parts[0]);
						Double temperature = Double.valueOf(parts[1]);
						saveTemperatureDeviation(aircraftModel, altitude, temperature, type, phase);
					} catch (NumberFormatException e) {
						throw AircraftEnvironmentLimitationsCustomApplicationException.csvParsingError(
								"Error parsing number for model " + aircraftModel.getModel() + ": " + e.getMessage());
					}
				});

	}

	private void saveTemperatureDeviation(AircraftModelEntity aircraftModel, Double altitude, Double temperature,
			String type, String phase) {
		TemperatureDeviationEntity deviation = new TemperatureDeviationEntity();
		deviation.setAircraftModel(aircraftModel);
		deviation.setAltitude(altitude);
		deviation.setTemperature(temperature);
		deviation.setType(type);
		deviation.setPhase(phase);
		temperatureDeviationRepository.save(deviation);
	}
}