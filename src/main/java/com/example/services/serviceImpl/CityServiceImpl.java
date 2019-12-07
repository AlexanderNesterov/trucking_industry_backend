package com.example.services.serviceImpl;

import com.example.controller.exceptions.SavingCityException;
import com.example.database.models.City;
import com.example.database.repositories.CityRepository;
import com.example.services.CityService;
import com.example.services.mappers.CityMapper;
import com.example.services.models.CityDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityServiceImpl(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    @Override
    public List<CityDto> findAll() {
        return cityMapper.toListDto(cityRepository.findAll());
    }

    @Override
    public List<CityDto> findCitiesByListId(Long[] listId) {
        return cityMapper.toListDto(cityRepository.getCitiesById(listId));
    }

    @Override
    public boolean addCity(CityDto city) {
        List<City> existCities = cityRepository.getCitiesByNameAndCountry(city.getName(), city.getCountry());

        if (existCities.size() != 0) {
            throw new SavingCityException("City with this name or country already exists");
        }

        cityRepository.save(cityMapper.fromDto(city));
        return false;
    }
}
