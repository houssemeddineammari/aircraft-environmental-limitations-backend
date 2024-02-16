package com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemperatureDeviationEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Double altitude;
	private Double temperature;
	private String phase;
	private String type;

	@ManyToOne
	@JoinColumn(name = "aircraft_model_id")
	private AircraftModelEntity aircraftModel;

}
