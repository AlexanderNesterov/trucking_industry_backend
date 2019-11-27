package com.example.database.models;

import com.example.database.models.commons.CargoStatus;

import javax.persistence.*;

@Entity
@Table(name = "cargo")
public class Cargo {

    @Id
    @SequenceGenerator(name = "cargo_seq", sequenceName = "cargo_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cargo_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "load_location_id")
    private int loadLocation;

    @Column(name = "discharge_location_id")
    private int dischargeLocation;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "co_driver_id")
    private Driver coDriver;

    @Column(name = "weight")
    private double weight;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private CargoStatus status;

    public Cargo() {
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

    public int getLoadLocation() {
        return loadLocation;
    }

    public void setLoadLocation(int loadLocation) {
        this.loadLocation = loadLocation;
    }

    public int getDischargeLocation() {
        return dischargeLocation;
    }

    public void setDischargeLocation(int dischargeLocation) {
        this.dischargeLocation = dischargeLocation;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Driver getCoDriver() {
        return coDriver;
    }

    public void setCoDriver(Driver coDriver) {
        this.coDriver = coDriver;
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
