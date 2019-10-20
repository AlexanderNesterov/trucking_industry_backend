package com.freight.repositories;

import com.freight.models.Truck;
import com.freight.repositories.custom.CustomTruck;
import org.springframework.data.repository.CrudRepository;

public interface TruckRepository extends CrudRepository<Truck, Integer>, CustomTruck<Truck> {

}
