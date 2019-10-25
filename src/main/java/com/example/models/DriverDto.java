package com.example.models;

import com.example.database.models.commons.DriverStatus;
import com.example.models.validation.annotation.IncorrectDriverLicense;

public class DriverDto {

    private int id;

    @IncorrectDriverLicense
    private String driverLicense;

    private DriverStatus status;

    private UserDto userDto;

    public DriverDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
