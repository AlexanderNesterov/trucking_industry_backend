package com.freight.serviceImpl;

import com.freight.DAO.CityDAO;
import com.freight.models.City;
import com.freight.services.CityService;
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
