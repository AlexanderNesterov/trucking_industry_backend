package com.example.services.models;

import com.example.database.models.commons.DriverStatus;

public class FullInfoDriverDto {

    private Long id;
    private String driverLicense;
    private DriverStatus status;
    private FullInfoUserDto user;

    public FullInfoDriverDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public FullInfoUserDto getUser() {
        return user;
    }

    public void setUser(FullInfoUserDto user) {
        this.user = user;
    }
}
