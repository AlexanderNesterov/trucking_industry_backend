package com.example.DAO;

import com.example.models.CitiesDistance;

import java.util.List;

public interface CitiesDistanceDAO {

    CitiesDistance findById(int citiesDistanceId);
    List<CitiesDistance> findAll();
    CitiesDistance updateCitiesDistance(CitiesDistance citiesDistance);
    void addCitiesDistance(CitiesDistance citiesDistance);
    void deleteCitiesDistanceById(int citiesDistanceId);
}
