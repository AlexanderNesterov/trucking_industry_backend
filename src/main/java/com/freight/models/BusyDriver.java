package com.freight.models;

import javax.persistence.*;

@Entity
@Table(name = "busy_drivers", schema = "freight")
public class BusyDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "driver_id")
    private int driverId;

    @Column(name = "order_id")
    private int orderId;

    public BusyDriver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
