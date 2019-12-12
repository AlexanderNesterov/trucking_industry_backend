package com.example.services.models;

import com.example.database.models.commons.DriverStatus;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.example.services.serviceImpl.validation.Message.*;

public class SimpleDriverDto {

    @NotNull(message = ID + IS_NULL)
    @Min(value = 1, message = ID + INVALID_FORMAT)
    private Long id;

    @NotNull(message = DRIVER_LICENSE + IS_NULL)
    @Pattern(regexp = "\\d{10}", message = DRIVER_LICENSE + INVALID_FORMAT)
    private String driverLicense;
    private DriverStatus status;

    @Valid
    private SimpleUserDto user;

    public SimpleDriverDto() {
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

    public SimpleUserDto getUser() {
        return user;
    }

    public void setUser(SimpleUserDto user) {
        this.user = user;
    }
}
