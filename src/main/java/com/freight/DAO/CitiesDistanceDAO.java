package com.freight.DAO;

import com.freight.models.CitiesDistance;
import java.util.List;

public interface CitiesDistanceDAO {

    CitiesDistance findById(int citiesDistanceId);
    List<CitiesDistance> findAll();
    CitiesDistance updateCitiesDistance(CitiesDistance citiesDistance);
    void addCitiesDistance(CitiesDistance citiesDistance);
    void deleteCitiesDistanceById(int citiesDistanceId);
}
