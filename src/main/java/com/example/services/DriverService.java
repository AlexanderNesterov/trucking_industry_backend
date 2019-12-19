package com.example.services;

import com.example.database.models.commons.DriverStatus;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;

import javax.validation.Valid;
import java.util.List;

public interface DriverService {

    /**
     * @return {@link com.example.services.models.SimpleDriverDto}
     */
    SimpleDriverDto findById(Long driverId);

    List<SimpleDriverDto> getFreeDrivers(String text, int page, int size);

    /**
     * @param text     text from search string
     * @param page     number of page
     * @param pageSize size of page
     * @return list of {@link com.example.services.models.SimpleDriverDto}
     */
    List<SimpleDriverDto> getDrivers(String text, int page, int pageSize);

    /**
     * @return null if driver with id isn't free else {@link com.example.services.models.SimpleDriverDto}
     */
    SimpleDriverDto getFreeDriver(Long driverId);

    /**
     * @return true if driverLicence exists
     */
    boolean isDriverLicenseExists(String driverLicense, Long driverId);

    /**
     * @param driver {@link com.example.services.models.SimpleDriverDto}
     * @return true if driver successfully updated
     */
    boolean updateDriver(@Valid SimpleDriverDto driver);

    /**
     * @return true if driver successfully added
     */
    boolean addDriver(@Valid FullInfoDriverDto driver);


    boolean blockAccount(Long userId, Long driverId);

    /**
     * Set status to drivers
     *
     * @param driverIds array of driver id
     */
    void setDriverStatus(Long[] driverIds, DriverStatus status);
}
