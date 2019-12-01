package com.example.services.mappers;

import com.example.database.models.City;
import com.example.services.models.CityDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City fromDto(CityDto cityDto);
    CityDto toDto(City city);
    List<City> fromListDto(List<CityDto> cityDtoList);
    List<CityDto> toListDto(List<City> cityList);
}
