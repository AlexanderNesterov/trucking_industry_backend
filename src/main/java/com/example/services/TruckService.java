package com.example.services;

import com.example.services.models.TruckDto;

import java.util.List;

public interface TruckService {

    TruckDto findById(Long truckDtoId);
    List<TruckDto> findAll();
    boolean updateTruck(TruckDto truckDto);
    List<TruckDto> getFreeTrucks(double weight);
    boolean addTruck(TruckDto truckDto);
}
