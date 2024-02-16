package com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.TemperatureDeviationEntity;

@Repository
public interface TemperatureDeviationRepository extends JpaRepository<TemperatureDeviationEntity, Long> {

}
