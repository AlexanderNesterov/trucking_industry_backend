package com.example.services.mappers;

import com.example.database.models.Cargo;
import com.example.models.CargoDto;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CargoMapper {

    Cargo fromDto(CargoDto cargoDto);
    CargoDto toDto(Cargo cargo);
    List<Cargo> fromListDto(List<CargoDto> cargoDtos);
    List<CargoDto> toListDto(List<Cargo> cargos);
}
