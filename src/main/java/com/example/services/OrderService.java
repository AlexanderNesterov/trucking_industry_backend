package com.example.services;

import com.example.database.models.Order;
import com.example.services.models.OrderDto;

import javax.validation.Valid;
import java.util.List;

public interface OrderService {

    /**
     * @return {@link com.example.services.models.OrderDto}
     */
    OrderDto findById(Long orderId);

    /**
     * @param text     text from search string
     * @param page     number of page
     * @param pageSize size of page
     * @return list of {@link com.example.services.models.OrderDto}
     */
    List<OrderDto> getOrders(String text, int page, int pageSize);

    OrderDto getOrderByDriverId(Long driverId);

    List<Order> getOrdersToSendMail();

    /**
     * @param orderDto {@link com.example.services.models.OrderDto}
     * @return true if order successfully added
     */
    boolean addOrder(@Valid OrderDto orderDto);

    /**
     * @param orderDto {@link com.example.services.models.OrderDto}
     * @return true if order successfully updated
     */
    boolean updateOrder(@Valid OrderDto orderDto);

    boolean setAcceptStatus(Long orderId, Long driverId);

    boolean setRefuseStatus(Long orderId, Long driverId);

    boolean setCanceledStatus(Long orderId);

    /**
     * Set flag emailSent to true
     */
    void setEmailSent(Long orderId);

    void tryToSetDeliverStatus(Long orderId);
}
