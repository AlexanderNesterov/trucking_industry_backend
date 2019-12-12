package com.example.database.models;

import com.example.database.models.commons.OrderStatus;
import org.hibernate.annotations.Cascade;
import org.springframework.context.annotation.Configuration;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @SequenceGenerator(name = "order_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @OneToOne
    @JoinColumn(name = "co_driver_id")
    private Driver coDriver;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "order_id")
    private List<Cargo> cargoList;

    @Column(name = "total_weight")
    private double totalWeight;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "search_string")
    private String searchString;

    @Column(name = "send_mail")
    private boolean isSendMail;

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
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

    public boolean isSendMail() {
        return isSendMail;
    }

    public void setSendMail(boolean sendMail) {
        isSendMail = sendMail;
    }
}
