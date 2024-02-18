package com.racemusconsulting.aircraftenvironmentallimitationsbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationRequestDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos.TemperatureDeviationResponseDTO;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.services.TemperatureDeviationService;

@RestController
@RequestMapping("/performance")
public class TemperatureDeviationController {

    private final TemperatureDeviationService temperatureDeviationService;

    @Autowired
    public TemperatureDeviationController(TemperatureDeviationService temperatureDeviationService) {
        this.temperatureDeviationService = temperatureDeviationService;
    }

    @PostMapping("/temperatureDeviation")
    public ResponseEntity<TemperatureDeviationResponseDTO> getTemperatureDeviation(
            @RequestBody TemperatureDeviationRequestDTO request) {
    	TemperatureDeviationResponseDTO deviationDTO = temperatureDeviationService.getTemperatureDeviation(
                request.getAcModel(), request.getAltitude(), request.getAeroPhase());
        return ResponseEntity.ok(deviationDTO);
    }
}
