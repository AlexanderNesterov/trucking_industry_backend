package com.example.services.models;

import com.example.database.models.commons.OrderStatus;

import java.util.List;

public class OrderDto {
    private Long id;
    private TruckDto truck;
    private SimpleDriverDto driver;
    private SimpleDriverDto coDriver;
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

    public SimpleDriverDto getDriver() {
        return driver;
    }

    public void setDriver(SimpleDriverDto driver) {
        this.driver = driver;
    }

    public SimpleDriverDto getCoDriver() {
        return coDriver;
    }

    public void setCoDriver(SimpleDriverDto coDriver) {
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
