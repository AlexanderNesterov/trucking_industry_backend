package com.example.services;

import com.example.services.models.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto findById(Long orderId);
    List<OrderDto> findAll();
    OrderDto getOrderByDriverId(Long orderId);
    boolean addOrder(OrderDto orderDto);
    boolean updateOrder(OrderDto orderDto);
    boolean setAcceptStatus(Long orderId, Long driverId);
    boolean setRefuseStatus(Long orderId, Long driverId);
    boolean setDeliverStatus(Long orderId, Long driverId);
    boolean setCanceledStatus(Long orderId);
}
