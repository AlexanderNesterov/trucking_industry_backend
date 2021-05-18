package com.example.database.repositories;

import com.example.database.models.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("from Order o where o.status in (" +
            "com.example.database.models.commons.OrderStatus.CREATED, " +
            "com.example.database.models.commons.OrderStatus.IN_PROGRESS)" +
            " and (o.driver.id = :driverId or o.coDriver.id = :driverId)")
    Optional<Order> getOrderByDriverId(@Param("driverId") Long driverId);

    @Query("from Order o where o.id = :orderId and o.driver.id = :driverId")
    Optional<Order> getOrderToChangeStatus(@Param("orderId") Long orderId, @Param("driverId") Long driverId);

    @Query("from Order o where o.id = :orderId and o.status = " +
            "com.example.database.models.commons.OrderStatus.REFUSED_BY_DRIVER")
    Order getOrderToUpdate(@Param("orderId") Long orderId);

    @Query("from Order o where o.id = :orderId and o.status not in (" +
            "com.example.database.models.commons.OrderStatus.DELIVERED," +
            "com.example.database.models.commons.OrderStatus.CANCELED)")
    Order getOrderToCancel(@Param("orderId") Long orderId);

    @Query("from Order o where o.searchString like %:text%")
    List<Order> getOrders(@Param("text") String text, Pageable pageable);

    @Query("from Order o where o.isSendMail = false " +
            "and o.status = com.example.database.models.commons.OrderStatus.CREATED")
    List<Order> getOrdersToSendMail();

    @Transactional
    @Modifying
    @Query("update Order o set o.searchString = :searchString where o.id = :orderId")
    void setOrderSearchString(@Param("searchString") String searchString, @Param("orderId") Long orderId);

    @Transactional
    @Modifying
    @Query("update Order o set o.isSendMail = true where o.id = :orderId")
    void setSentEmail(@Param("orderId") Long orderId);

    @Query("from Order o where o.status = com.example.database.models.commons.OrderStatus.IN_PROGRESS")
    List<Order> getInProgressOrders();
}
