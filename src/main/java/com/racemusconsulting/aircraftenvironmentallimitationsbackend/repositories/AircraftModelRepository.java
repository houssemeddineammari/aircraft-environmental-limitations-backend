package com.racemusconsulting.aircraftenvironmentallimitationsbackend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.racemusconsulting.aircraftenvironmentallimitationsbackend.entities.AircraftModelEntity;

@Repository
public interface AircraftModelRepository extends JpaRepository<AircraftModelEntity, Long> {
    
}