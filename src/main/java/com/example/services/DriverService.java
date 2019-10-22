package com.example.services;

import com.example.models.DriverDto;
import javax.validation.Valid;
import java.util.List;

public interface DriverService {
    
    DriverDto findById(int driverId);
    List<DriverDto> findAll();
    DriverDto updateDriver(@Valid DriverDto driver);
    void addDriver(@Valid DriverDto driver);
    void deleteDriverById(int driverId);
}
