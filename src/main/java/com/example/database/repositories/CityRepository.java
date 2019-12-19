package com.example.database.repositories;

import com.example.database.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("from City c where c.id in (:listId)")
    List<City> getCitiesById(@Param("listId") Long[] id);

    List<City> getCitiesByNameAndCountry(String name, String country);
}
