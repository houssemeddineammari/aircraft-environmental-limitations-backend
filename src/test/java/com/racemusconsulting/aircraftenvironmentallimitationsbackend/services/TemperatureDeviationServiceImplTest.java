package com.racemusconsulting.aircraftenvironmentallimitationsbackend.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.racemusconsulting.aircraftenvironmentallimitationsbackend.exceptions.AircraftEnvironmentLimitationsCustomApplicationException;

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

}
