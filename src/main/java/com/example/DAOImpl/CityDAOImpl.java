package com.example.DAOImpl;

import com.example.DAO.CityDAO;
import com.example.models.City;
import com.example.repositories.CityRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CityDAOImpl implements CityDAO {

    private final CityRepository cityRepository;

    public CityDAOImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City findById(int cityId) {
        return cityRepository.findById(cityId).get();
    }

    @Override
    public List<City> findAll() {
        List<City> cities = new ArrayList<>();
        cityRepository.findAll().forEach(cities::add);

        return cities;
    }

    @Override
    public City updateCity(City city) {
        return cityRepository.save(city);
    }

    @Override
    public void addCity(City city) {
        city.setId(0);
        cityRepository.save(city);
    }

    @Override
    public void deleteCityById(int cityId) {
        cityRepository.deleteById(cityId);
    }
}
