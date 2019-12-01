package com.example.database.repositories;

import com.example.database.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS)" +
            " and (o.driver.id = :driverId or o.coDriver.id = :driverId)")
    Order getOrderByDriverId(@Param("driverId") Long driverId);

    @Query("from Order o where o.id = :orderId and o.driver.id = :driverId")
    Order getOrderToChangeStatus(@Param("orderId") Long orderId, @Param("driverId") Long driverId);

    @Query("from Order o where o.id = :orderId and o.status = " +
            "com.example.database.models.commons.OrderStatus.REFUSED_BY_DRIVER")
    Order getOrderToUpdate(@Param("orderId") Long orderId);

    @Query("from Order o where o.id = :orderId and o.status not in (" +
            "com.example.database.models.commons.OrderStatus.DELIVERED," +
            "com.example.database.models.commons.OrderStatus.CANCELED)")
    Order getOrderToCancel(@Param("orderId") Long orderId);
}
