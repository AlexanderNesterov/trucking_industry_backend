package com.freight.DAO;

import com.freight.models.Order;
import java.util.List;

public interface OrderDAO {

    Order findById(int orderId);
    List<Order> findAll();
    Order updateOrder(Order order);
    void addOrder(Order order);
    void deleteOrderById(int orderId);
}
