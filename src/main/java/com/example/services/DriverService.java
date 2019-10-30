package com.example.services;

import com.example.models.DriverDto;
import javax.validation.Valid;
import java.util.List;

public interface DriverService {
    
    DriverDto findById(int driverDtoId);
    List<DriverDto> findAll();
    List<DriverDto> getFreeDrivers();
    DriverDto updateDriver(@Valid DriverDto driver);
    void addDriver(@Valid DriverDto driver);
}
