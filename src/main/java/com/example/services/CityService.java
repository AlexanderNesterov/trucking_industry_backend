package com.example.services;

import com.example.services.models.CityDto;

import java.util.List;

public interface CityService {

    /**
     * @return list of all cities
     */
    List<CityDto> findAll();

    /**
     * @param listId list of cities id
     * @return list of cities
     */
    List<CityDto> findCitiesByListId(Long[] listId);

    /**
     * @return true if city successfully added
     */
    boolean addCity(CityDto city);
}
