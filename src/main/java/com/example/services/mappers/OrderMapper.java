package com.example.services.mappers;

import com.example.database.models.Order;
import com.example.services.models.OrderDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "searchString", ignore = true)
    OrderDto toDto(Order order);

    @Mappings({
            @Mapping(target = "searchString", ignore = true),
            @Mapping(target = "cargoList", ignore = true)
    })
    OrderDto toDtoWithoutCargoList(Order order);

    Order fromDto(OrderDto orderDto);

    default List<OrderDto> toListDtoWithoutCargoList(List<Order> orders) {
        if (orders == null) {
            return null;
        }

        return orders.stream()
                .map(this::toDtoWithoutCargoList)
                .collect(Collectors.toList());
    }
}
