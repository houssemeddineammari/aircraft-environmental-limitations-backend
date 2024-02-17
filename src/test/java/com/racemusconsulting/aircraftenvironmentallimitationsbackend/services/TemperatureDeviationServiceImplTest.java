package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.AircraftModelEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions.AircraftEnvironmentLimitationsCustomApplicationException;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.TemperatureDeviationRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TemperatureDeviationServiceImplTest {

	@Autowired
	private TemperatureDeviationService temperatureDeviationService;

	@Test
	public void whenModelIsNull_thenThrowException() {
		assertThrows(AircraftEnvironmentLimitationsCustomApplicationException.class, () -> {
			temperatureDeviationService.getTemperatureDeviation(null, 10000.0, "TAKEOFF");
		});
	}

	@Test
	public void whenAltitudeIsNull_thenThrowException() {
		assertThrows(AircraftEnvironmentLimitationsCustomApplicationException.class, () -> {
			temperatureDeviationService.getTemperatureDeviation("A320", null, "TAKEOFF");
		});
	}

	@Test
	public void whenPhaseIsNull_thenThrowException() {
		assertThrows(AircraftEnvironmentLimitationsCustomApplicationException.class, () -> {
			temperatureDeviationService.getTemperatureDeviation("A320", 10000.0, null);
		});
	}

	@Test
	public void whenValidModelAndPhase_thenFindTemperatureDeviations() {
		TemperatureDeviationRepository mockRepository = Mockito.mock(TemperatureDeviationRepository.class);
		AircraftModelEntity aircraftModel = new AircraftModelEntity();
		aircraftModel.setId(1L); // Supposons un ID arbitraire pour l'exemple
		aircraftModel.setModel("A320");

		TemperatureDeviationEntity deviationMin = new TemperatureDeviationEntity(1L, 10000.0, 5.0, "CRUISE", "MIN",
				aircraftModel);
		TemperatureDeviationEntity deviationMax = new TemperatureDeviationEntity(2L, 10000.0, 15.0, "CRUISE", "MAX",
				aircraftModel);

		Mockito.when(mockRepository.findByAircraftModelModelAndPhase("A320", "CRUISE"))
				.thenReturn(Arrays.asList(deviationMin, deviationMax));

		TemperatureDeviationServiceImpl service = new TemperatureDeviationServiceImpl(mockRepository);
		TemperatureDeviationResponseDTO response = service.getTemperatureDeviation("A320", 10000.0, "CRUISE");
		assertNotNull(response);
		assertEquals(5.0, response.getMinTemperature());
		assertEquals(15.0, response.getMaxTemperature());
	}

	@Test
	public void whenExactMatchFound_thenReturnDirectTemperature() {

		TemperatureDeviationRepository mockRepository = Mockito.mock(TemperatureDeviationRepository.class);

		AircraftModelEntity aircraftModel = new AircraftModelEntity();
		aircraftModel.setModel("A320");
		TemperatureDeviationEntity directMatchMin = new TemperatureDeviationEntity();
		directMatchMin.setAltitude(10000.0);
		directMatchMin.setTemperature(10.0);
		directMatchMin.setPhase("CRUISE");
		directMatchMin.setType("MIN");
		directMatchMin.setAircraftModel(aircraftModel);

		TemperatureDeviationEntity directMatchMax = new TemperatureDeviationEntity();
		directMatchMax.setAltitude(10000.0);
		directMatchMax.setTemperature(20.0);
		directMatchMax.setPhase("CRUISE");
		directMatchMax.setType("MAX");
		directMatchMax.setAircraftModel(aircraftModel);

		List<TemperatureDeviationEntity> deviations = Arrays.asList(directMatchMin, directMatchMax);

		Mockito.when(mockRepository.findByAircraftModelModelAndPhase("A320", "CRUISE")).thenReturn(deviations);

		TemperatureDeviationServiceImpl service = new TemperatureDeviationServiceImpl(mockRepository);

		TemperatureDeviationResponseDTO response = service.getTemperatureDeviation("A320", 10000.0, "CRUISE");

		assertEquals(10.0, response.getMinTemperature());
		assertEquals(20.0, response.getMaxTemperature());
	}

}
