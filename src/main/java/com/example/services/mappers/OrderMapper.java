package com.example.services.mappers;

import com.example.database.models.Cargo;
import com.example.database.models.Order;
import com.example.services.models.CargoDto;
import com.example.services.models.OrderDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order fromDto(OrderDto orderDto);
    OrderDto toDto(Order order);
    List<Order> fromListDto(List<OrderDto> orderDtoList);
    List<OrderDto> toListDto(List<Order> orderList);
}
