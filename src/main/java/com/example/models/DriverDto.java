package com.example.models;

import com.example.database.models.commons.DriverStatus;
import com.example.models.validation.annotation.IncorrectDriverLicense;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class DriverDto {

    private int id;

    @NotBlank
    @Length(max = 20)
    private String firstName;

    @NotBlank
    @Length(max = 20)
    private String lastName;

    @IncorrectDriverLicense
    private String driverLicense;

    @Min(0)
    private double hoursPerMonth;

    private DriverStatus status;

    public DriverDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getHoursPerMonth() {
        return hoursPerMonth;
    }

    public void setHoursPerMonth(double hoursPerMonth) {
        this.hoursPerMonth = hoursPerMonth;
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
}
