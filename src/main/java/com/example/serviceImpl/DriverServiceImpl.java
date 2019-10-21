package com.example.serviceImpl;

import com.example.DAO.DriverDAO;
import com.example.models.Driver;
import com.example.services.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {

    private final DriverDAO driverDAO;

    public DriverServiceImpl(DriverDAO driverDAO) {
        this.driverDAO = driverDAO;
    }

    @Override
    public Driver findById(int driverId) {
        return driverDAO.findById(driverId);
    }

    @Override
    public List<Driver> findAll() {
        return driverDAO.findAll();
    }

    @Override
    public Driver updateDriver(Driver driver) {
        return driverDAO.updateDriver(driver);
    }

    @Override
    public void addDriver(Driver driver) {
        driverDAO.addDriver(driver);
    }

    @Override
    public void deleteDriverById(int driverId) {
        driverDAO.deleteDriverById(driverId);
    }
}
