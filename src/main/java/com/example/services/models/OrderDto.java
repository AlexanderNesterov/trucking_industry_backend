package com.example.services.models;

import com.example.database.models.commons.OrderStatus;
import com.example.services.models.interfaces.Searchable;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

import static com.example.services.serviceImpl.validation.Message.*;

public class OrderDto implements Searchable {
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

    @Override
    public void combineSearchString() {
        StringBuilder sb = new StringBuilder();

        sb
                .append(id).append(" ")
                .append(driver.getUser().getFirstName()).append(" ")
                .append(driver.getUser().getLastName()).append(" ")
                .append(coDriver.getUser().getFirstName()).append(" ")
                .append(coDriver.getUser().getLastName()).append(" ")
                .append(truck.getRegistrationNumber()).append(" ")
                .append(totalWeight).append(" ")
                .append(status).append(" ");
        cargoList.forEach(cargoDto -> sb.append(cargoDto.getTitle()).append(" "));
        cargoList.forEach(cargoDto -> sb.append(cargoDto.getLoadLocation().getName()).append(" "));
        cargoList.forEach(cargoDto -> sb.append(cargoDto.getDischargeLocation().getName()).append(" "));

        searchString = sb.toString().toLowerCase();
    }
}
