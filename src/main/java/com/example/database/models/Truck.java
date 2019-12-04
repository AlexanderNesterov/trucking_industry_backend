package com.example.database.models;

import com.example.database.models.commons.TruckCondition;
import javax.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @SequenceGenerator(name = "truck_seq", sequenceName = "trucks_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "truck_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "model")
    private String model;

    @Column(name = "capacity")
    private double capacity;

    @Column(name = "condition")
    @Enumerated(value = EnumType.STRING)
    private TruckCondition condition;

    @Column(name = "search_string")
    private String searchString;

    public Truck() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
