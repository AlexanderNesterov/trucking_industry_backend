package com.example.models;

import javax.persistence.*;

@Entity
@Table(name = "trucks", schema = "freight")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "reg_num")
    private String registrationNumber;

    @Column(name = "shift_size")
    private int shiftSize;

    @Column(name = "capacity")
    private double capacity;

    @Column(name = "condition")
    private boolean condition;

    @Column(name = "city_id")
    private int cityId;

    public Truck() {
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

    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "registrationNumber='" + registrationNumber + '\'' +
                ", shiftSize=" + shiftSize +
                ", capacity=" + capacity +
                ", condition=" + condition +
                '}';
    }
}
