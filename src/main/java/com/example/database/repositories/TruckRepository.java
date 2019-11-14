package com.example.database.repositories;

import com.example.database.models.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Integer> {
    @Query("from Truck t where t.capacity >= :weight and t.condition = 'SERVICEABLE' and t.id not in " +
            "(select t.id from Cargo c join c.truck t where " +
            "c.status in ('CREATED', 'IN_PROGRESS', 'REFUSED_BY_DRIVER'))")
    List<Truck> getFreeTrucks(@Param("weight") double weight);

    Truck getTruckByRegistrationNumber(String registrationNumber);
}
