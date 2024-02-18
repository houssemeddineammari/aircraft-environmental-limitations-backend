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

	@Test
	public void whenInterpolationIsRequired_thenInterpolateCorrectly() {
		TemperatureDeviationRepository mockRepository = Mockito.mock(TemperatureDeviationRepository.class);
		AircraftModelEntity aircraftModel = new AircraftModelEntity();
		aircraftModel.setModel("A320");

		TemperatureDeviationEntity lowerDeviationMin = new TemperatureDeviationEntity(1L, 9000.0, -5.0, "CRUISE", "MIN",
				aircraftModel);
		TemperatureDeviationEntity upperDeviationMin = new TemperatureDeviationEntity(2L, 11000.0, -15.0, "CRUISE",
				"MIN", aircraftModel);
		TemperatureDeviationEntity lowerDeviationMax = new TemperatureDeviationEntity(3L, 9000.0, 20.0, "CRUISE", "MAX",
				aircraftModel);
		TemperatureDeviationEntity upperDeviationMax = new TemperatureDeviationEntity(4L, 11000.0, 25.0, "CRUISE",
				"MAX", aircraftModel);

		List<TemperatureDeviationEntity> deviations = Arrays.asList(lowerDeviationMin, upperDeviationMin,
				lowerDeviationMax, upperDeviationMax);
		Mockito.when(mockRepository.findByAircraftModelModelAndPhase("A320", "CRUISE")).thenReturn(deviations);

		TemperatureDeviationServiceImpl service = new TemperatureDeviationServiceImpl(mockRepository);

		TemperatureDeviationResponseDTO response = service.getTemperatureDeviation("A320", 10000.0, "CRUISE");

		assertEquals(-10.0, response.getMinTemperature(), 0.01);
		assertEquals(22.5, response.getMaxTemperature(), 0.01);
	}
	
	@Test
	public void whenNoTemperatureDeviationsFound_thenThrowTemperatureDeviationNotFoundException() {
	    TemperatureDeviationRepository mockRepository = Mockito.mock(TemperatureDeviationRepository.class);
	    Mockito.when(mockRepository.findByAircraftModelModelAndPhase("A320", "CRUISE")).thenReturn(Collections.emptyList());

	    TemperatureDeviationServiceImpl service = new TemperatureDeviationServiceImpl(mockRepository);

	    assertThrows(AircraftEnvironmentLimitationsCustomApplicationException.class, () -> {
	        service.getTemperatureDeviation("A320", 10000.0, "CRUISE");
	    });
	}

	@Test
	public void whenCannotInterpolateTemperatures_thenThrowTemperatureInterpolationException() {
	    TemperatureDeviationRepository mockRepository = Mockito.mock(TemperatureDeviationRepository.class);
	    AircraftModelEntity aircraftModel = new AircraftModelEntity();
	    aircraftModel.setModel("A320");

	    TemperatureDeviationEntity deviationMin = new TemperatureDeviationEntity(1L, 9000.0, -5.0, "CRUISE", "MIN", aircraftModel);
	    TemperatureDeviationEntity deviationMax = new TemperatureDeviationEntity(2L, 11000.0, 25.0, "CRUISE", "MAX", aircraftModel);

	    List<TemperatureDeviationEntity> deviations = Arrays.asList(deviationMin, deviationMax);
	    Mockito.when(mockRepository.findByAircraftModelModelAndPhase("A320", "CRUISE")).thenReturn(deviations);

	    TemperatureDeviationServiceImpl service = new TemperatureDeviationServiceImpl(mockRepository);

	    assertThrows(AircraftEnvironmentLimitationsCustomApplicationException.class, () -> {
	        service.getTemperatureDeviation("A320", 10001.0, "CRUISE");
	    });
	}
}
