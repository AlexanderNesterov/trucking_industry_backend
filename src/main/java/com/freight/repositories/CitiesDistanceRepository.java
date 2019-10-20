package com.freight.repositories;

import com.freight.models.CitiesDistance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitiesDistanceRepository extends CrudRepository<CitiesDistance, Integer> {
}
