package com.example.DAO;

import com.example.models.Order;
import java.util.List;

public interface OrderDAO {

    Order findById(int orderId);
    List<Order> findAll();
    Order updateOrder(Order order);
    void addOrder(Order order);
    void deleteOrderById(int orderId);
}
