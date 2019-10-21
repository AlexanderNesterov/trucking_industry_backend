package com.example.models;

import javax.persistence.*;

@Entity
@Table(name = "drivers", schema = "freight")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "hours_per_month")
    private double hoursPerMonth;

    @Column(name = "status")
    private String status;

    @Column(name = "city_id")
    private int cityId;

    @Column(name = "truck_id")
    private int truckId;

    public Driver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getHoursPerMonth() {
        return hoursPerMonth;
    }

    public void setHoursPerMonth(double hoursPerMonth) {
        this.hoursPerMonth = hoursPerMonth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }
}
