package com.example.services.mappers;

import com.example.database.models.User;
import com.example.services.models.UserDto;
import com.example.services.commons.IPasswordEncryptor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    private IPasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncoder(IPasswordEncryptor passwordEncoder) {
        this.passwordEncryptor = passwordEncoder;
    }

    String mapPassword(UserDto dto) {
        return passwordEncryptor.encrypt(dto.getPassword());
    }

    @Mapping(target = "password", expression = "java(mapPassword(userDto))")
    public abstract User fromDto(UserDto userDto);

    public abstract UserDto toDto(User user);
    public abstract List<User> fromListDto(List<UserDto> userDtos);
    public abstract List<UserDto> toListDto(List<User> users);
}
