package com.example.database.repositories;

import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    @Query("from Driver d where d.status = com.example.database.models.commons.DriverStatus.REST " +
            "and d.user.status = com.example.database.models.commons.AccountStatus.ACTIVE ")
    List<Driver> getFreeDrivers();

    @Query("from Driver d where d.user.login = :driverLogin")
    Driver getDriverByLogin(@Param("driverLogin") String driverLogin);

    @Query("from Driver d where d.id = :driverId " +
            "and d.status = com.example.database.models.commons.DriverStatus.REST " +
            "and d.user.status = com.example.database.models.commons.AccountStatus.ACTIVE")
    Driver getFreeDriver(@Param("driverId") Long driverId);

    @Query("from Driver d where d.searchString like %:text%")
    List<Driver> getDrivers(@Param("text") String text, Pageable pageable);

    @Query("select d.id from Driver d where d.driverLicense = :driverLicense")
    Long getDriverIdByDriverLicense(@Param("driverLicense") String driverLicense);

    @Transactional
    @Modifying
    @Query("update Driver d set d.status = :status where d.id in (:driverIds)")
    void setDriverStatus(@Param("driverIds") Long[] ids, @Param("status") DriverStatus status);
}
