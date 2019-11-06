package com.example.services;

import com.example.models.CargoDto;
import javax.validation.Valid;
import java.util.List;

public interface CargoService {

    CargoDto findById(int cargoId);
    List<CargoDto> findAll();
    CargoDto updateCargo(@Valid CargoDto cargoDto);
    CargoDto getCargoByDriverId(int driverId);
    void setAcceptStatus(int cargoId, int driverId);
    void setRefuseStatus(int cargoId, int driverId);
    void setDeliverStatus(int cargoId, int driverId);
    void addCargo(@Valid CargoDto cargoDto);
}
