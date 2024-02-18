package com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories;

import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;

@Repository
public interface TemperatureDeviationRepository extends JpaRepository<TemperatureDeviationEntity, Long> {
	List<TemperatureDeviationEntity> findByAircraftModelModel(String model);

	@Cacheable("temperatureDeviations")
	List<TemperatureDeviationEntity> findByAircraftModelModelAndPhase(String model, String phase);
}
