package com.example.services.models;

import com.example.database.models.commons.DriverStatus;
import com.example.services.models.interfaces.Searchable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.example.services.serviceImpl.validation.Message.*;

public class FullInfoDriverDto implements Searchable {

    private Long id;

    @NotNull(message = DRIVER_LICENSE + IS_NULL)
    @Pattern(regexp = "\\d{10}", message = DRIVER_LICENSE + INVALID_FORMAT)
    private String driverLicense;
    private DriverStatus status;

    @Valid
    private FullInfoUserDto user;
    private String searchString;

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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public void combineSearchString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(user.getFirstName()).append(" ")
                .append(user.getLastName()).append(" ")
                .append(user.getEmail()).append(" ")
                .append(user.getPhone()).append(" ")
                .append(driverLicense).append(" ")
                .append(status);

        searchString = sb.toString().toLowerCase();
    }
}
