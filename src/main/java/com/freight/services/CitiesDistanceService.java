package com.freight.services;

import com.freight.models.CitiesDistance;

import java.util.List;

public interface CitiesDistanceService {

    CitiesDistance findById(int citiesDistanceId);
    List<CitiesDistance> findAll();
    CitiesDistance updateCitiesDistance(CitiesDistance citiesDistance);
    void addCitiesDistance(CitiesDistance citiesDistance);
    void deleteCitiesDistanceById(int citiesDistanceId);
}
