package com.freight.DAO;

import com.freight.models.City;
import java.util.List;

public interface CityDAO {

    City findById(int cityId);
    List<City> findAll();
    City updateCity(City city);
    void addCity(City city);
    void deleteCityById(int cityId);
}
