package com.example.services.serviceImpl;

import com.example.database.repositories.CityRepository;
import com.example.services.CityService;
import com.example.services.mappers.CityMapper;
import com.example.services.models.CityDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        return cityRepository
                .findAll()
                .stream()
                .map(cityMapper::toDto)
                .collect(Collectors.toList());
    }
}
