package com.freight.services;

import com.freight.models.Order;
import java.util.List;

public interface OrderService {

    Order findById(int orderId);
    List<Order> findAll();
    Order updateOrder(Order order);
    void addOrder(Order order);
    void deleteOrderById(int orderId);
}
