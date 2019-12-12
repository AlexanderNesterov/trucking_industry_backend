package com.example.services.mappers;

import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleDriverDto;
import com.example.database.models.Driver;
import com.example.services.models.SimpleUserDto;
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

    String mapPassword(FullInfoUserDto dto) {
        return passwordEncryptor.encrypt(dto.getPassword());
    }

    public abstract SimpleDriverDto toDto(Driver driver);
    public abstract Driver fromDto(SimpleDriverDto driver);
    public abstract List<Driver> fromListDto(List<SimpleDriverDto> driverDtos);
    public abstract List<SimpleDriverDto> toListDto(List<Driver> drivers);

    @Mapping(target = "user.password", expression = "java(mapPassword(fullInfoUserDto))")
    public abstract Driver fromFullInfoDto(FullInfoDriverDto driverDto);
    public abstract FullInfoDriverDto toFullInfoDto(Driver driver);
}
