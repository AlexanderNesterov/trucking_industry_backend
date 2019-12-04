package com.example.services;

import com.example.services.models.TruckDto;

import java.util.List;

public interface TruckService {

    TruckDto findById(Long truckDtoId);
    List<TruckDto> findAll(int page, int pageSize);
    List<TruckDto> getTrucksBySearch(String text);
    List<TruckDto> getFreeTrucks(double weight);
    TruckDto getFreeTruck(Long truckId, double weight);
    boolean updateTruck(TruckDto truckDto);
    boolean addTruck(TruckDto truckDto);
}
