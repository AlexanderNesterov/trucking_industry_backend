package com.freight.DAOImpl;

import com.freight.DAO.OrderDAO;
import com.freight.models.Order;
import com.freight.repositories.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private final OrderRepository orderRepository;

    public OrderDAOImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findById(int orderId) {
        return orderRepository.findById(orderId).get();
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll().forEach(orders::add);

        return orders;
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(int orderId) {
        orderRepository.deleteById(orderId);
    }
}
