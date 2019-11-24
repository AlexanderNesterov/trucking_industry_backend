package com.example.services.mappers;

import com.example.database.models.Truck;
import com.example.services.models.TruckDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TruckMapper {

    Truck fromDto(TruckDto truckDto);
    TruckDto toDto(Truck truck);
    List<Truck> fromListDto(List<TruckDto> trucksDtos);
    List<TruckDto> toListDto(List<Truck> trucks);
}
