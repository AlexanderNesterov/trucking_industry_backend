package com.example.serviceImpl;

import com.example.DAO.OrderDAO;
import com.example.models.Order;
import com.example.services.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;

    public OrderServiceImpl(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public Order findById(int orderId) {
        return orderDAO.findById(orderId);
    }

    @Override
    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    @Override
    public Order updateOrder(Order order) {
        return orderDAO.updateOrder(order);
    }

    @Override
    public void addOrder(Order order) {
        orderDAO.addOrder(order);
    }

    @Override
    public void deleteOrderById(int orderId) {
        orderDAO.deleteOrderById(orderId);
    }
}
