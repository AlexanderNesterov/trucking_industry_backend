package com.example.services.mappers;

import com.example.database.models.Cargo;
import com.example.models.CargoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CargoMapper {

    @Mapping(target = "driverStatus", source = "cargoDto.driverStatus")
    Cargo fromDto(CargoDto cargoDto);

    @Mapping(target = "driverStatus", source = "cargo.driverStatus")
    CargoDto toDto(Cargo cargo);
    List<Cargo> fromListDto(List<CargoDto> cargoDtos);
    List<CargoDto> toListDto(List<Cargo> cargos);
}
