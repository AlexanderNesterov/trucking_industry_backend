package com.example.services.models;

import com.example.database.models.commons.OrderStatus;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.example.services.serviceImpl.validation.Message.*;

public class OrderDto {
    private Long id;
    private TruckDto truck;
    private SimpleDriverDto driver;
    private SimpleDriverDto coDriver;

    @Valid
    private List<CargoDto> cargoList;

    @NotNull(message = TOTAL_WEIGHT + IS_NULL)
    @DecimalMin(value = "1", message = TOTAL_WEIGHT + TOO_SMALL + T_WEIGHT_MIN_VALUE)
    @DecimalMax(value = "1000000", message = TOTAL_WEIGHT + TOO_BIG + T_WEIGHT_MAX_VALUE)
    private double totalWeight;
    private OrderStatus status;
    private String searchString;
    private List<CityDto> theBestWay;

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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public List<CityDto> getTheBestWay() {
        return theBestWay;
    }

    public void setTheBestWay(List<CityDto> theBestWay) {
        this.theBestWay = theBestWay;
    }
}
