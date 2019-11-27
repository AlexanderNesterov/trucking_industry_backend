package com.example.database.repositories;

import com.example.database.models.Truck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long> {
    @Query("from Truck t where t.capacity >= :weight " +
            "and t.condition = com.example.database.models.commons.TruckCondition.SERVICEABLE " +
            "and t.id not in " +
            "(select t.id from Cargo c join c.truck t where " +
            "c.status in (" +
            "com.example.database.models.commons.CargoStatus.CREATED, " +
            "com.example.database.models.commons.CargoStatus.IN_PROGRESS))")
    List<Truck> getFreeTrucks(@Param("weight") double weight);

    Truck getTruckByRegistrationNumber(String registrationNumber);

    @Query("from Truck t where t.id = :truckId and t.capacity >= :cargoWeight " +
            "and t.condition = com.example.database.models.commons.TruckCondition.SERVICEABLE " +
            "and t.id not in (select c.truck.id from Cargo c where c.status in (" +
            "com.example.database.models.commons.CargoStatus.CREATED, " +
            "com.example.database.models.commons.CargoStatus.IN_PROGRESS))")
    Truck getFreeTruck(@Param("truckId") Long truckId, @Param("cargoWeight") double cargoWeight);
}
