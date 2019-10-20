package com.freight.services;

import com.freight.models.Truck;

import java.util.List;

public interface TruckService {

    Truck findById(int truckId);
    List<Truck> findAll();
    Truck updateTruck(Truck truck);
    void addTruck(Truck truck);
    void deleteTruckById(int truckId);

    List<Object> getSpecs(int truckId);
}
