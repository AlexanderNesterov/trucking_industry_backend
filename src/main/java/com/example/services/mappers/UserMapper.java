package com.example.services.mappers;

import com.example.database.models.User;
import com.example.models.UserDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDto(UserDto userDto);
    UserDto toDto(User user);
    List<User> fromListDto(List<UserDto> userDtos);
    List<UserDto> toListDto(List<User> users);
}
