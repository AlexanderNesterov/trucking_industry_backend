package com.example.services;

import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;

import javax.validation.Valid;
import java.util.List;

public interface DriverService {
    
    FullInfoDriverDto findById(Long driverDtoId);
    List<SimpleDriverDto> getFreeDrivers();
    List<SimpleDriverDto> getDrivers(String text, int page, int pageSize);
    SimpleDriverDto getFreeDriver(Long driverId);
    boolean updateDriver(@Valid FullInfoDriverDto driver);
    boolean addDriver(@Valid FullInfoDriverDto driver);
}
