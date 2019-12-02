package com.example.services.mappers;

import com.example.database.models.User;
import com.example.services.models.SimpleUserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
interface UserMapper {

    SimpleUserDto toDto(User user);
    List<User> fromListDto(List<SimpleUserDto> userDtos);
    List<SimpleUserDto> toListDto(List<User> users);
}
