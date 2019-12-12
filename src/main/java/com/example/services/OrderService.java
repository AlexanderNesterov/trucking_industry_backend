package com.example.services;

import com.example.database.models.Order;
import com.example.services.models.OrderDto;

import javax.validation.Valid;
import java.util.List;

public interface OrderService {

    OrderDto findById(Long orderId);
    List<OrderDto> getOrders(String text, int page, int size);
    OrderDto getOrderByDriverId(Long orderId);
    List<Order> getOrdersToSendMail();
//    boolean checkOrderToBlockDriver(Long driverId);
    boolean addOrder(@Valid OrderDto orderDto);
    boolean updateOrder(@Valid OrderDto orderDto);
    boolean setAcceptStatus(Long orderId, Long driverId);
    boolean setRefuseStatus(Long orderId, Long driverId);
    boolean setCanceledStatus(Long orderId);
    void setEmailSent(Long orderId);
    void tryToSetDeliverStatus(Long orderId);
}
