package com.example.services.mappers;

import com.example.database.models.User;
import com.example.services.commons.IPasswordEncryptor;
import com.example.services.models.FullInfoUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class FullInfoUserMapper {
    private IPasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncoder(IPasswordEncryptor passwordEncoder) {
        this.passwordEncryptor = passwordEncoder;
    }

    String mapPassword(FullInfoUserDto dto) {
        return passwordEncryptor.encrypt(dto.getPassword());
    }

    @Mapping(target = "password", expression = "java(mapPassword(userDto))")
    public abstract User fromFullInfoDto(FullInfoUserDto userDto);
    public abstract FullInfoUserDto toFullInfoDto(User user);
}
