package com.example.database.repositories;

import com.example.database.models.Driver;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DriverRepository extends CrudRepository<Driver, Integer> {

    @Query("from Driver d where d.status = 'REST' and d.id not in " +
            "(select d.id from Cargo c join c.driver d where c.status = 'CREATED' or c.status = 'INPROGRESS')" +
            "and d.id not in " +
            "(select cd.id from Cargo c join c.coDriver cd where c.status = 'CREATED' or c.status = 'INPROGRESS')")
    List<Driver> getFreeDrivers();
}
