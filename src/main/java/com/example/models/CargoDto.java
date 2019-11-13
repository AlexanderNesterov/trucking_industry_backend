package com.example.models;

import com.example.database.models.commons.CargoStatus;

public class CargoDto {

    private int id;

    private String title;

    private String description;

    private int loadLocation;

    private int dischargeLocation;

    private TruckDto truckDto;

    private DriverDto driverDto;

    private DriverDto coDriverDto;

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
