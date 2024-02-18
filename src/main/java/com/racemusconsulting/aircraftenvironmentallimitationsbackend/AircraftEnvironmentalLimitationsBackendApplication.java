package com.racemusconsulting.aircraftenvironmentallimitationsbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AircraftEnvironmentalLimitationsBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AircraftEnvironmentalLimitationsBackendApplication.class, args);
	}

}
