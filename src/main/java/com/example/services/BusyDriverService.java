package com.example.services;

import com.example.models.BusyDriver;

import java.util.List;

public interface BusyDriverService {

    BusyDriver findById(int busyDriverId);
    List<BusyDriver> findAll();
    BusyDriver updateBusyDriver(BusyDriver busyDriver);
    void addBusyDriver(BusyDriver busyDriver);
    void deleteBusyDriverById(int busyDriverId);
}
