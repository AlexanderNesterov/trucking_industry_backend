package com.freight.DAOImpl;

import com.freight.DAO.DriverDAO;
import com.freight.models.Driver;
import com.freight.repositories.DriverRepository;
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
