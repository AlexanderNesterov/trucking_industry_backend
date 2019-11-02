package com.example.models;

import com.example.database.models.commons.CargoStatus;
import com.example.database.models.commons.DriverCargoStatus;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CargoDto {

    private int id;

    @NotBlank
    @Length(max = 32)
    private String title;

    @Length(max = 256)
    private String description;

    private int loadLocation;

    private int dischargeLocation;

    @NotNull
    private TruckDto truckDto;

    private DriverCargoStatus driverStatus;

    private DriverCargoStatus coDriverStatus;

    @NotNull
    private DriverDto driverDto;

    @NotNull
    private DriverDto coDriverDto;

    @Min(1)
    private double weight;

    private CargoStatus status;

    public CargoDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLoadLocation() {
        return loadLocation;
    }

    public void setLoadLocation(int loadLocation) {
        this.loadLocation = loadLocation;
    }

    public int getDischargeLocation() {
        return dischargeLocation;
    }

    public void setDischargeLocation(int dischargeLocation) {
        this.dischargeLocation = dischargeLocation;
    }

    public TruckDto getTruckDto() {
        return truckDto;
    }

    public void setTruckDto(TruckDto truckDto) {
        this.truckDto = truckDto;
    }

    public DriverDto getDriverDto() {
        return driverDto;
    }

    public void setDriverDto(DriverDto driverDto) {
        this.driverDto = driverDto;
    }

    public DriverDto getCoDriverDto() {
        return coDriverDto;
    }

    public void setCoDriverDto(DriverDto coDriverDto) {
        this.coDriverDto = coDriverDto;
    }

    public DriverCargoStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverCargoStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public DriverCargoStatus getCoDriverStatus() {
        return coDriverStatus;
    }

    public void setCoDriverStatus(DriverCargoStatus coDriverStatus) {
        this.coDriverStatus = coDriverStatus;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public CargoStatus getStatus() {
        return status;
    }

    public void setStatus(CargoStatus status) {
        this.status = status;
    }
}
