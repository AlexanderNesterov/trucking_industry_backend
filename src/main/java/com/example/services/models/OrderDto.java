package com.example.services.models;

import com.example.database.models.commons.OrderStatus;

import java.util.List;

public class OrderDto {
    private Long id;
    private TruckDto truck;
    private DriverDto driver;
    private DriverDto coDriver;
    private List<CargoDto> cargoList;
    private double totalWeight;
    private OrderStatus status;

    public OrderDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<CargoDto> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<CargoDto> cargoList) {
        this.cargoList = cargoList;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
