package com.freight.serviceImpl;

import com.freight.DAO.OrderDAO;
import com.freight.models.Order;
import com.freight.services.OrderService;
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
