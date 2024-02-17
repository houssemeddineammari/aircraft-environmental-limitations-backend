package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.AircraftModelEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.AircraftModelRepository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories.TemperatureDeviationRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataLoadingFromCsvServiceTest {

	@Autowired
	private DataLoadingFromCsvService dataLoadingService;

	@MockBean
	private AircraftModelRepository aircraftModelRepository;

	@MockBean
	private TemperatureDeviationRepository temperatureDeviationRepository;

	@Test
	public void testLoadCsvData() throws IOException {
		String csvContent = "aircraft_Id;maximum_temperature_deviation_in_flight;minimum_temperature_deviation_in_flight;maximum_temperature_deviation_in_TOLD;minimum_temperature_deviation_in_TOLD\n"
				+ "MD808321;-2000.0 : 35.0|2500.0 : 35.0|2501.0 : 40.0|8500.0 : 40.0|8501.0 : 35.0|37000.0 : 35.0;"
				+ "-2000.0 : -69.0|8500.0 : -69.0|8501.0 : -74.0|36089.0 : -20.0|37000.0 : -20.0;"
				+ "-2000.0 : 35.0|2500.0 : 35.0|2501.0 : 40.0|8500.0 : 40.0;" + "-2000.0 : -69.0|8500.0 : -69.0";
		ByteArrayInputStream bais = new ByteArrayInputStream(csvContent.getBytes());

		ClassPathResource resource = mock(ClassPathResource.class);
		when(resource.getInputStream()).thenReturn(bais);

		AircraftModelEntity mockModel = new AircraftModelEntity();
		mockModel.setId(1L);
		when(aircraftModelRepository.save(any(AircraftModelEntity.class))).thenReturn(mockModel);

		TemperatureDeviationEntity mockDeviation = new TemperatureDeviationEntity();
		when(temperatureDeviationRepository.save(any(TemperatureDeviationEntity.class))).thenReturn(mockDeviation);

		dataLoadingService.loadCsvData();
		verify(temperatureDeviationRepository, atLeastOnce()).save(any(TemperatureDeviationEntity.class));
	}
}
