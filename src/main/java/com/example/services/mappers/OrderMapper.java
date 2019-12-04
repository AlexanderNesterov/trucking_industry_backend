package com.example.services.mappers;

import com.example.database.models.Order;
import com.example.services.models.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Order fromDto(OrderDto orderDto);

    @Mapping(target = "searchString", ignore = true)
    OrderDto toDto(Order order);
    List<Order> fromListDto(List<OrderDto> orderDtoList);
    List<OrderDto> toListDto(List<Order> orderList);
}
