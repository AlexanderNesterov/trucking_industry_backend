package com.example.services.models;

import com.example.database.models.commons.CargoStatus;

public class CargoDto {

    private Long id;
    private String title;
    private String description;
    private CityDto loadLocation;
    private CityDto dischargeLocation;
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

    public CityDto getLoadLocation() {
        return loadLocation;
    }

    public void setLoadLocation(CityDto loadLocation) {
        this.loadLocation = loadLocation;
    }

    public CityDto getDischargeLocation() {
        return dischargeLocation;
    }

    public void setDischargeLocation(CityDto dischargeLocation) {
        this.dischargeLocation = dischargeLocation;
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
