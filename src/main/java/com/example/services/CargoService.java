package com.example.services;

import com.example.models.Cargo;

import java.util.List;

public interface CargoService {

    Cargo findById(int cargoId);
    List<Cargo> findAll();
    Cargo updateCargo(Cargo cargo);
    void addCargo(Cargo cargo);
    void deleteCargoById(int cargoId);
}
