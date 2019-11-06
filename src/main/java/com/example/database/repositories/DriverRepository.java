package com.example.database.repositories;

import com.example.database.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {

    @Query("from Driver d where d.status = 'REST' and d.id not in " +
            "(select d.id from Cargo c join c.driver d where c.status = 'CREATED' or c.status = 'INPROGRESS' " +
            "or c.status = 'REFUSED_BY_DRIVER')" +
            "and d.id not in " +
            "(select cd.id from Cargo c join c.coDriver cd where c.status = 'CREATED' or c.status = 'INPROGRESS' " +
            "or c.status = 'REFUSED_BY_DRIVER')")
    List<Driver> getFreeDrivers();
}
