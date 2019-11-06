package com.example.database.repositories;

import com.example.database.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {

    @Query("from Cargo c where (c.status = 'CREATED' or c.status = 'REFUSED_BY_DRIVER')" +
            " and (c.driver.id = :driverId or c.coDriver.id = :driverId)")
    Cargo getCargoByDriverId(@Param("driverId") int driverId);

    @Query("from Cargo c where c.status = 'CREATED'")
    List<Cargo> getCreatedCargoList();
}
