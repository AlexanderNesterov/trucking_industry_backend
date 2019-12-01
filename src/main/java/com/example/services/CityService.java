package com.example.services;

import com.example.services.models.CityDto;

import java.util.List;

public interface CityService {
    List<CityDto> findAll();
}
