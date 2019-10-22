package com.example.services.mappers;

import com.example.models.DriverDto;
import com.example.modelsDB.Driver;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DriverMapper {

    Driver fromDto(DriverDto driverDto);
    DriverDto toDto(Driver driver);
    List<Driver> fromListDto(List<DriverDto> driverDtos);
    List<DriverDto> toListDto(List<Driver> drivers);
}
