package com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos;

import lombok.Data;

@Data
public class TemperatureDeviationRequestDTO {
	private String acModel;
    private Double altitude;
    private String aeroPhase;

}
