package com.example.services;

import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;

import java.util.List;

public interface DriverService {
    
    FullInfoDriverDto findById(Long driverDtoId);
    List<SimpleDriverDto> findAll();
    List<SimpleDriverDto> getFreeDrivers();
    SimpleDriverDto getFreeDriver(Long driverId);
    boolean updateDriver(FullInfoDriverDto driver);
    boolean addDriver(FullInfoDriverDto driver);
}
