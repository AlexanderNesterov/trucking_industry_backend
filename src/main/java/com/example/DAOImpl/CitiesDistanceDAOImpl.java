package com.example.DAOImpl;

import com.example.DAO.CitiesDistanceDAO;
import com.example.models.CitiesDistance;
import com.example.repositories.CitiesDistanceRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CitiesDistanceDAOImpl implements CitiesDistanceDAO {

    private final CitiesDistanceRepository citiesDistanceRepository;

    public CitiesDistanceDAOImpl(CitiesDistanceRepository citiesDistanceRepository) {
        this.citiesDistanceRepository = citiesDistanceRepository;
    }

    @Override
    public CitiesDistance findById(int citiesDistanceId) {
        return citiesDistanceRepository.findById(citiesDistanceId).get();
    }

    @Override
    public List<CitiesDistance> findAll() {
        List<CitiesDistance> citiesDistances = new ArrayList<>();
        citiesDistanceRepository.findAll().forEach(citiesDistances::add);

        return citiesDistances;
    }

    @Override
    public CitiesDistance updateCitiesDistance(CitiesDistance citiesDistance) {
        return citiesDistanceRepository.save(citiesDistance);
    }

    @Override
    public void addCitiesDistance(CitiesDistance citiesDistance) {
        citiesDistanceRepository.save(citiesDistance);
    }

    @Override
    public void deleteCitiesDistanceById(int citiesDistanceId) {
        citiesDistanceRepository.deleteById(citiesDistanceId);
    }
}
