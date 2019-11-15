package com.example.services;

import com.example.models.CargoDto;
import java.util.List;

public interface CargoService {

    CargoDto findById(Long cargoId);
    List<CargoDto> findAll();
    CargoDto getCargoByDriverId(int driverId);
    CargoDto getCargoByTruckId(int truckId);
    boolean addCargo(CargoDto cargoDto);
    boolean updateCargo(CargoDto cargoDto);
    boolean setAcceptStatus(Long cargoId, int driverId);
    boolean setRefuseStatus(Long cargoId, int driverId);
    boolean setDeliverStatus(Long cargoId, int driverId);
}
