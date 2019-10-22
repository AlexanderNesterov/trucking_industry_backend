package com.example.serviceImpl;

import com.example.models.CitiesDistance;
import com.example.database.DAO.CitiesDistanceDAO;
import com.example.services.CitiesDistanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CitiesDistanceServiceImpl implements CitiesDistanceService {

    private final CitiesDistanceDAO citiesDistanceDAO;

    public CitiesDistanceServiceImpl(CitiesDistanceDAO citiesDistanceDAO) {
        this.citiesDistanceDAO = citiesDistanceDAO;
    }

    @Override
    public CitiesDistance findById(int citiesDistanceId) {
        return citiesDistanceDAO.findById(citiesDistanceId);
    }

    @Override
    public List<CitiesDistance> findAll() {
        return citiesDistanceDAO.findAll();
    }

    @Override
    public CitiesDistance updateCitiesDistance(CitiesDistance citiesDistance) {
        return citiesDistanceDAO.updateCitiesDistance(citiesDistance);
    }

    @Override
    public void addCitiesDistance(CitiesDistance citiesDistance) {
        citiesDistanceDAO.addCitiesDistance(citiesDistance);
    }

    @Override
    public void deleteCitiesDistanceById(int citiesDistanceId) {
        citiesDistanceDAO.deleteCitiesDistanceById(citiesDistanceId);
    }
}
