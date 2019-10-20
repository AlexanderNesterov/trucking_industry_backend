package com.freight.services;

import com.freight.models.City;

import java.util.List;

public interface CityService {

    City findById(int cityId);
    List<City> findAll();
    City updateCity(City city);
    void addCity(City city);
    void deleteCityById(int cityId);
}
