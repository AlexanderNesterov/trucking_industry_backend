package com.example.services;

import com.example.models.DriverDto;
import java.util.List;

public interface DriverService {
    
    DriverDto findById(Long driverDtoId);
    List<DriverDto> findAll();
    List<DriverDto> getFreeDrivers();
    DriverDto getFreeDriver(Long driverId);
    boolean updateDriver(DriverDto driver);
    boolean addDriver(DriverDto driver);
}
