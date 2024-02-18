package com.racemusconsulting.aircraftenvironmentallimitationsbackend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.services.TemperatureDeviationService;

@RestController
@RequestMapping("/performance")
public class TemperatureDeviationController {

    private final TemperatureDeviationService temperatureDeviationService;

    @Autowired
    public TemperatureDeviationController(TemperatureDeviationService temperatureDeviationService) {
        this.temperatureDeviationService = temperatureDeviationService;
    }

    
}
