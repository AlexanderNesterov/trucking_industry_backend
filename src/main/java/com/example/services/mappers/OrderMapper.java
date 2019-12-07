package com.example.services.mappers;

import com.example.database.models.Order;
import com.example.services.models.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "searchString", ignore = true)
    OrderDto toDto(Order order);
    Order fromDto(OrderDto orderDto);
    List<Order> fromListDto(List<OrderDto> orderDtoList);
    List<OrderDto> toListDto(List<Order> orderList);
}
