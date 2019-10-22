package com.example.serviceImpl;

import com.example.database.DAO.CityDAO;
import com.example.models.City;
import com.example.services.CityService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityDAO cityDAO;

    public CityServiceImpl(CityDAO cityDAO) {
        this.cityDAO = cityDAO;
    }

    @Override
    public City findById(int cityId) {
        return cityDAO.findById(cityId);
    }

    @Override
    public List<City> findAll() {
        return cityDAO.findAll();
    }

    @Override
    public City updateCity(City city) {
        return cityDAO.updateCity(city);
    }

    @Override
    public void addCity(City city) {
        cityDAO.addCity(city);
    }

    @Override
    public void deleteCityById(int cityId) {
        cityDAO.deleteCityById(cityId);
    }
}
