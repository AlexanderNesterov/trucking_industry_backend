package com.example.database.models;

import com.example.database.models.commons.TruckCondition;
import javax.persistence.*;

@Entity
@Table(name = "trucks")
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
