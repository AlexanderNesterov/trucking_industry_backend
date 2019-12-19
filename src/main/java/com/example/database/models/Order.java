package com.example.database.models;

import com.example.database.models.commons.OrderStatus;
import com.example.database.models.interfaces.Searchable;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order implements Searchable {

    @Id
    @SequenceGenerator(name = "order_seq", sequenceName = "orders_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "co_driver_id")
    private Driver coDriver;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
