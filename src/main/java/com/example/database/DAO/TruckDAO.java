package com.example.database.DAO;

import com.example.database.models.Truck;
import java.util.List;

public interface TruckDAO {

    Truck findById(int truckId);
    List<Truck> findAll();
    Truck updateTruck(Truck truck);
    List<Truck> getFreeTrucks(double weight);
    void addTruck(Truck truck);
    void deleteTruckById(int truckId);
}
