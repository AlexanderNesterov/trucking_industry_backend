package com.example.models;

import com.example.database.models.commons.TruckCondition;
import com.example.models.validation.annotation.IncorrectRegistrationNumber;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class TruckDto {

    private int id;

    @IncorrectRegistrationNumber
    private String registrationNumber;

    @NotBlank
    private String model;

    @Min(1)
    private int shiftSize;

    @Min(0)
    private double capacity;

    private TruckCondition condition;

    public TruckDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public int getShiftSize() {
        return shiftSize;
    }

    public void setShiftSize(int shiftSize) {
        this.shiftSize = shiftSize;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public TruckCondition getCondition() {
        return condition;
    }

    public void setCondition(TruckCondition condition) {
        this.condition = condition;
    }
}
