package com.example.services.models;

import com.example.database.models.commons.CargoStatus;

public class CargoDto {

    private Long id;
    private String title;
    private String description;
    private int loadLocation;
    private int dischargeLocation;
    private TruckDto truck;
    private DriverDto driver;
    private DriverDto coDriver;
    private double weight;
    private CargoStatus status;

    public CargoDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public TruckDto getTruck() {
        return truck;
    }

    public void setTruck(TruckDto truck) {
        this.truck = truck;
    }

    public DriverDto getDriver() {
        return driver;
    }

    public void setDriver(DriverDto driver) {
        this.driver = driver;
    }

    public DriverDto getCoDriver() {
        return coDriver;
    }

    public void setCoDriver(DriverDto coDriver) {
        this.coDriver = coDriver;
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
