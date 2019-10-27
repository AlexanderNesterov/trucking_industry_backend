package com.example.database.DAOImpl;

import com.example.database.DAO.DriverDAO;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
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
        Driver driver = driverRepository.findById(driverId).get();
        driver.getUser().setPassword(null);

        return driver;
    }

    @Override
    public List<Driver> findAll() {
        List<Driver> drivers = new ArrayList<>();
        driverRepository.findAll().forEach(drivers::add);

        for (Driver driver : drivers) {
            driver.getUser().setPassword(null);
        }

        return drivers;
    }

    @Override
    public Driver updateDriver(Driver driver) {
        Driver currentInfo = driverRepository.findById(driver.getId()).get();
        driver.getUser().setPassword(currentInfo.getUser().getPassword());

        return driverRepository.save(driver);
    }

    @Override
    public void addDriver(Driver driver) {
        driver.setStatus(DriverStatus.REST);
        driver.getUser().setRole(Role.DRIVER);
        driverRepository.save(driver);
    }

    @Override
    public void deleteDriverById(int driverId) {
        driverRepository.deleteById(driverId);
    }
}
