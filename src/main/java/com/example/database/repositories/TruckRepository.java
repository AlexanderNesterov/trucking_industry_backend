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
            "(select t.id from Order o join o.truck t where " +
            "o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS))")
    List<Truck> getFreeTrucks(@Param("weight") double weight);

    Truck getTruckByRegistrationNumber(String registrationNumber);

    @Query("from Truck t where t.id = :truckId and t.capacity >= :orderWeight " +
            "and t.condition = com.example.database.models.commons.TruckCondition.SERVICEABLE " +
            "and t.id not in (select o.truck.id from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS))")
    Truck getFreeTruck(@Param("truckId") Long truckId, @Param("orderWeight") double orderWeight);

    @Query("from Truck t where t.searchString like %:text%")
    List<Truck> getTrucksBySearch(@Param("text") String text);
}
