package com.example.services.mappers;

import com.example.database.models.User;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleUserDto;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    private IPasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncoder(IPasswordEncryptor passwordEncoder) {
        this.passwordEncryptor = passwordEncoder;
    }

    public String mapPassword(String password) {
        return passwordEncryptor.encrypt(password);
    }

    public abstract SimpleUserDto toDto(User user);
    public abstract List<User> fromListDto(List<SimpleUserDto> userDtos);
    public abstract List<SimpleUserDto> toListDto(List<User> users);
}
