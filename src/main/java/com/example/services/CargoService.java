package com.example.services;

import com.example.models.CargoDto;
import javax.validation.Valid;
import java.util.List;

public interface CargoService {

    CargoDto findById(int cargoId);
    List<CargoDto> findAll();
    CargoDto updateCargo(@Valid CargoDto cargoDto);
    void addCargo(@Valid CargoDto cargoDto);
}
