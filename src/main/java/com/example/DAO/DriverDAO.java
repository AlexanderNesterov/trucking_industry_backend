package com.example.DAO;

import com.example.modelsDB.Driver;
import java.util.List;

public interface DriverDAO {

    Driver findById(int driverId);
    List<Driver> findAll();
    Driver updateDriver(Driver driver);
    void addDriver(Driver driver);
    void deleteDriverById(int driverId);
}
