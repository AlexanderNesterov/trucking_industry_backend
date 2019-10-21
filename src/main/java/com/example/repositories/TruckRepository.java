package com.example.repositories;

import com.example.repositories.custom.CustomTruck;
import com.example.models.Truck;
import org.springframework.data.repository.CrudRepository;

public interface TruckRepository extends CrudRepository<Truck, Integer>, CustomTruck<Truck> {

}
