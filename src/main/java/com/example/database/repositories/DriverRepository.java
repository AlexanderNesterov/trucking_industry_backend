package com.example.database.repositories;

import com.example.database.models.Driver;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    @Query("from Driver d where d.status = 'REST' " +
            "and d.id not in (select o.driver.id from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS)) " +
            "and d.id not in (select o.coDriver.id from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS))")
    List<Driver> getFreeDrivers();

    Driver getDriverByDriverLicense(String driverLicense);

    @Query("from Driver d where d.user.login = :driverLogin")
    Driver getDriverByLogin(@Param("driverLogin") String driverLogin);

    @Query("from Driver d where d.status = 'REST' and d.id = :driverId " +
            "and d.id not in (select o.driver.id from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS)) " +
            "and d.id not in (select o.coDriver.id from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS))")
    Driver getFreeDriver(@Param("driverId") Long driverId);

    @Query("from Driver d where d.searchString like %:text%")
    List<Driver> getDrivers(@Param("text") String text, Pageable pageable);
}
