package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions.AircraftEnvironmentLimitationsCustomApplicationException;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.TemperatureDeviationRepository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.utils.MathUtils;

@Service
public class TemperatureDeviationServiceImpl implements TemperatureDeviationService {
	
	private static final Logger log = LoggerFactory.getLogger(TemperatureDeviationServiceImpl.class);
	private final TemperatureDeviationRepository repository;

	@Autowired
	public TemperatureDeviationServiceImpl(TemperatureDeviationRepository repository) {
		this.repository = repository;
	}

	@Override
	@Cacheable(value = "temperatureDeviations", key = "#model.concat('-').concat(#altitude.toString()).concat('-').concat(#phase)")
	public TemperatureDeviationResponseDTO getTemperatureDeviation(String model, Double altitude, String phase) {
		log.info("Calculating temperature deviation for model: {}, altitude: {}, phase: {}", model, altitude, phase);
		validateInputParameters(model, altitude, phase);
		List<TemperatureDeviationEntity> deviations = repository.findByAircraftModelModelAndPhase(model, phase);

		if (deviations.isEmpty()) {
			throw AircraftEnvironmentLimitationsCustomApplicationException.temperatureDeviationNotFound(
					"Aucune déviation de température trouvée pour le modèle d'avion et la phase spécifiés.");
		}

		deviations.sort(Comparator.comparingDouble(TemperatureDeviationEntity::getAltitude));

		Double minTemperature = findTemperature(deviations, altitude, "MIN");
		Double maxTemperature = findTemperature(deviations, altitude, "MAX");

		if (minTemperature == null || maxTemperature == null) {
			throw AircraftEnvironmentLimitationsCustomApplicationException.temperatureInterpolationException(
					"Impossible de trouver ou d'interpoler les températures min/max pour l'altitude donnée.");
		}

		TemperatureDeviationResponseDTO deviationDTO = new TemperatureDeviationResponseDTO();
		deviationDTO.setMinTemperature(MathUtils.roundToTwoDecimalPlaces(minTemperature));
		deviationDTO.setMaxTemperature(MathUtils.roundToTwoDecimalPlaces(maxTemperature));

		return deviationDTO;
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

	private Double findTemperature(List<TemperatureDeviationEntity> deviations, Double altitude, String type) {
		TemperatureDeviationEntity lower = null, upper = null;
		for (TemperatureDeviationEntity deviation : deviations) {
			if (type.equals(deviation.getType())) {
				if (deviation.getAltitude().equals(altitude)) {
					return deviation.getTemperature();
				} else if (deviation.getAltitude() < altitude) {
					lower = deviation;
				} else if (deviation.getAltitude() > altitude && upper == null) {
					upper = deviation;
					break;
				}
			}
		}

		if (lower != null && upper != null) {
			return interpolateTemperature(lower, upper, altitude);
		}

		return null;
	}

	private Double interpolateTemperature(TemperatureDeviationEntity lower, TemperatureDeviationEntity upper,
			Double altitude) {
		double ratio = (altitude - lower.getAltitude()) / (upper.getAltitude() - lower.getAltitude());
		return lower.getTemperature() + ratio * (upper.getTemperature() - lower.getTemperature());
	}
}
