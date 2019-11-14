package com.example.database.repositories;

import com.example.database.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    @Query("from Cargo c where c.status in ('CREATED', 'IN_PROGRESS')" +
            " and (c.driver.id = :driverId or c.coDriver.id = :driverId)")
    Cargo getCargoByDriverId(@Param("driverId") int driverId);

    @Query("from Cargo c where c.truck.id = :truckId and c.status in ('CREATED', 'IN_PROGRESS', 'REFUSED_BY_DRIVER')")
    Cargo getCargoByTruckId(@Param("truckId") int truckId);
}
