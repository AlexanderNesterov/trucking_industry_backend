package com.example.database.repositories;

import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    @Query("from Driver d where d.status = 'REST' " +
            "and d.id not in (select c.driver.id from Cargo c where c.status in ('CREATED', 'IN_PROGRESS')) " +
            "and d.id not in (select c.coDriver.id from Cargo c where c.status in ('CREATED', 'IN_PROGRESS'))")
    List<Driver> getFreeDrivers();

    Driver getDriverByDriverLicense(String driverLicense);

    @Query("from Driver d where d.user.login = :driverLogin")
    Driver getDriverByLogin(@Param("driverLogin") String driverLogin);
}
