package com.racemusconsulting.aircraftenvironmentallimitationsbackend.dtos;

import lombok.Data;

@Data
public class TemperatureDeviationResponseDTO {
	private Double minTemperature;
	private Double maxTemperature;

}
