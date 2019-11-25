package com.example.database.repositories;

import com.example.database.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Query("from Cargo c where c.status in ('CREATED', 'IN_PROGRESS')" +
            " and (c.driver.id = :driverId or c.coDriver.id = :driverId)")
    Cargo getCargoByDriverId(@Param("driverId") Long driverId);

    @Query("from Cargo c where c.truck.id = :truckId and c.status in ('CREATED', 'IN_PROGRESS', 'REFUSED_BY_DRIVER')")
    Cargo getCargoByTruckId(@Param("truckId") Long truckId);

    @Query("from Cargo c where c.id = :cargoId and c.driver.id = :driverId")
    Cargo getCargoToChangeStatus(@Param("cargoId") Long cargoId, @Param("driverId") Long driverId);

    @Query("from Cargo c where c.id = :cargoId and c.status = 'REFUSED_BY_DRIVER'")
    Cargo getCargoToUpdate(@Param("cargoId") Long cargoId);
}
