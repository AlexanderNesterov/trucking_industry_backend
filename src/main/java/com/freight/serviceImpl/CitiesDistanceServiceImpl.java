package com.freight.serviceImpl;

import com.freight.DAO.CitiesDistanceDAO;
import com.freight.models.CitiesDistance;
import com.freight.services.CitiesDistanceService;
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
