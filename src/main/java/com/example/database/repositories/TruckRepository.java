package com.example.database.repositories;

import com.example.database.models.Truck;
import org.springframework.data.repository.CrudRepository;

public interface TruckRepository extends CrudRepository<Truck, Integer> {

}
