package com.example.services;

import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;

import javax.validation.Valid;
import java.util.List;

public interface DriverService {
    
    FullInfoDriverDto findById(Long driverDtoId);
    List<SimpleDriverDto> findAll(int page, int pageSize);
    List<SimpleDriverDto> getFreeDrivers();
    List<SimpleDriverDto> getDriversBySearch(String text);
    SimpleDriverDto getFreeDriver(Long driverId);
    boolean updateDriver(@Valid FullInfoDriverDto driver);
    boolean addDriver(@Valid FullInfoDriverDto driver);
}
