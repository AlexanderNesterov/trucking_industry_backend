package com.example.database.DAOImpl;

import com.example.database.DAO.DriverDAO;
import com.example.database.models.Driver;
import com.example.database.repositories.DriverRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DriverDAOImpl implements DriverDAO {

    private final DriverRepository driverRepository;

    public DriverDAOImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Driver findById(int driverId) {
        return driverRepository.findById(driverId).get();
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();
        driverRepository.findAll().forEach(drivers::add);

        return drivers;
    }

    @Override
    public Driver updateDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public void addDriver(Driver driver) {
        driverRepository.save(driver);
    }

    @Override
    public void deleteDriverById(int driverId) {
        driverRepository.deleteById(driverId);
    }
}
