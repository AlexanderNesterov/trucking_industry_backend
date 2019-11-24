package com.example.services.mappers;

import com.example.services.models.DriverDto;
import com.example.database.models.Driver;
import com.example.services.models.UserDto;
import com.example.services.commons.IPasswordEncryptor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class DriverMapper {

    private IPasswordEncryptor passwordEncryptor;

    @Autowired
    public void setPasswordEncoder(IPasswordEncryptor passwordEncoder) {
        this.passwordEncryptor = passwordEncoder;
    }

    String mapPassword(UserDto dto) {
        return passwordEncryptor.encrypt(dto.getPassword());
    }

    @Mapping(target = "user.password", expression = "java(mapPassword(userDto))")
    public abstract Driver fromDto(DriverDto driverDto);
    public abstract DriverDto toDto(Driver driver);
    public abstract List<Driver> fromListDto(List<DriverDto> driverDtos);
    public abstract List<DriverDto> toListDto(List<Driver> drivers);
}
