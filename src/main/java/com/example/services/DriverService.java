package com.example.services;

import com.example.database.models.commons.DriverStatus;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;

import javax.validation.Valid;
import java.util.List;

public interface DriverService {
    
    SimpleDriverDto findById(Long driverDtoId);
    List<SimpleDriverDto> getFreeDrivers();
    List<SimpleDriverDto> getDrivers(String text, int page, int pageSize);
    SimpleDriverDto getFreeDriver(Long driverId);
    boolean isDriverLicenseExists(String driverLicense, Long driverId);
    boolean updateDriver(@Valid SimpleDriverDto driver);
    boolean addDriver(@Valid FullInfoDriverDto driver);
    void setDriverStatus(Long[] driverIds, DriverStatus status);
}
