package com.example.database.repositories;

import com.example.database.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargo, Long> {

    @Query("from Cargo c where c.id = :cargoId " +
            "and c.order.id = :orderId " +
            "and c.status = com.example.database.models.commons.CargoStatus.IN_PROGRESS " +
            "and (select o.driver.id from Order o where o.id = :orderId " +
            "and o.driver.id = :driverId " +
            "and o.driver.user.status = com.example.database.models.commons.AccountStatus.ACTIVE " +
            "and o.driver.status = com.example.database.models.commons.DriverStatus.ACTIVE " +
            "and o.status = com.example.database.models.commons.OrderStatus.IN_PROGRESS) = :driverId")
    Optional<Cargo> getCargoToDeliver(@Param("orderId") Long orderId, @Param("cargoId") Long cargoId,
                                      @Param("driverId") Long driverId);

    List<Cargo> getCargoByOrderId(Long orderId);
}
