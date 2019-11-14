package com.example.services;

import com.example.database.models.commons.DriverStatus;
import com.example.models.DriverDto;
import java.util.List;

public interface DriverService {
    
    DriverDto findById(int driverDtoId);
    List<DriverDto> findAll();
    List<DriverDto> getFreeDrivers();
    boolean updateDriver(DriverDto driver);
    boolean addDriver(DriverDto driver);
}
