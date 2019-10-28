package com.example.database.repositories;

import com.example.database.models.Truck;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends CrudRepository<Truck, Integer> {

    @Query("from Truck t where t.capacity >= :weight and t.condition = 0 and t.id not in " +
            "(select t.id from Cargo c join c.truck t where c.status = 0 or c.status = 1)")
    List<Truck> getFreeTrucks(@Param("weight") double weight);
}
