package com.example.services;

import com.example.models.City;

import java.util.List;

public interface CityService {

    City findById(int cityId);
    List<City> findAll();
    City updateCity(City city);
    void addCity(City city);
    void deleteCityById(int cityId);
}
