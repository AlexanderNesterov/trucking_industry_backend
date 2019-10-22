package com.example.database.DAO;

import com.example.models.Truck;
import java.util.List;

public interface TruckDAO {

    Truck findById(int truckId);
    List<Truck> findAll();
    Truck updateTruck(Truck truck);
    void addTruck(Truck truck);
    void deleteTruckById(int truckId);
}
