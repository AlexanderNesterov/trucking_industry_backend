package com.example.services;

import com.example.services.models.OrderDto;

public interface FindPathService {
    OrderDto findPath(OrderDto orderDto);
}
