package com.freight.services;

import com.freight.models.Driver;
import java.util.List;

public interface DriverService {
    
    Driver findById(int driverId);
    List<Driver> findAll();
    Driver updateDriver(Driver driver);
    void addDriver(Driver driver);
    void deleteDriverById(int driverId);
}
