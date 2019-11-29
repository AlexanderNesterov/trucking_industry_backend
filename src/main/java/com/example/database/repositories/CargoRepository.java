package com.example.database.repositories;

import com.example.database.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Query("from Cargo c where c.status in (" +
            "com.example.database.models.commons.CargoStatus.CREATED, " +
            "com.example.database.models.commons.CargoStatus.IN_PROGRESS)" +
            " and (c.driver.id = :driverId or c.coDriver.id = :driverId)")
    Cargo getCargoByDriverId(@Param("driverId") Long driverId);

    @Query("from Cargo c where c.id = :cargoId and c.driver.id = :driverId")
    Cargo getCargoToChangeStatus(@Param("cargoId") Long cargoId, @Param("driverId") Long driverId);

    @Query("from Cargo c where c.id = :cargoId and c.status = " +
            "com.example.database.models.commons.CargoStatus.REFUSED_BY_DRIVER")
    Cargo getCargoToUpdate(@Param("cargoId") Long cargoId);

    @Query("from Cargo c where c.id = :cargoId and c.status not in (" +
            "com.example.database.models.commons.CargoStatus.DELIVERED," +
            "com.example.database.models.commons.CargoStatus.CANCELED)")
    Cargo getCargoToCancel(@Param("cargoId") Long cargoId);
}
