package com.example.services.mappers;

import com.example.database.models.Cargo;
import com.example.models.CargoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CargoMapper {

    @Mappings({
            @Mapping(target = "truck", source = "truckDto"),
            @Mapping(target = "driver", source = "driverDto"),
            @Mapping(target = "coDriver", source = "coDriverDto"),
            @Mapping(target = "coDriver.user", source = "coDriverDto.userDto"),
            @Mapping(target = "driver.user", source = "driverDto.userDto")
    })
    Cargo fromDto(CargoDto cargoDto);

    @Mappings({
            @Mapping(target = "truckDto", source = "truck"),
            @Mapping(target = "driverDto", source = "driver"),
            @Mapping(target = "coDriverDto", source = "coDriver"),
            @Mapping(target = "coDriverDto.userDto", source = "coDriver.user"),
            @Mapping(target = "driverDto.userDto", source = "driver.user")
    })
    CargoDto toDto(Cargo cargo);
    List<Cargo> fromListDto(List<CargoDto> cargoDtos);
    List<CargoDto> toListDto(List<Cargo> cargos);
}
