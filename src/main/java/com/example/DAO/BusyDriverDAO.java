package com.example.DAO;

import com.example.models.BusyDriver;

import java.util.List;

public interface BusyDriverDAO {

    BusyDriver findById(int busyDriverId);
    List<BusyDriver> findAll();
    BusyDriver updateBusyDriver(BusyDriver busyDriver);
    void addBusyDriver(BusyDriver busyDriver);
    void deleteBusyDriverById(int busyDriverId);
}
