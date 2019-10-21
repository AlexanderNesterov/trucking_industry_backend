package com.example.repositories;

import com.example.models.City;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<City, Integer> {
}
