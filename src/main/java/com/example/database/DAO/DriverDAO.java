package com.example.database.DAO;

import com.example.database.models.Driver;
import java.util.List;

public interface DriverDAO {

    Driver findById(int driverId);
    List<Driver> findAll();
    List<Driver> getFreeDrivers();
    Driver updateDriver(Driver driver);
    void addDriver(Driver driver);
}
