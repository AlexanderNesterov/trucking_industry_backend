package com.example.serviceImpl;

import com.example.DAO.CargoDAO;
import com.example.models.Cargo;
import com.example.services.CargoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoServiceImpl implements CargoService {

    private final CargoDAO cargoDAO;

    public CargoServiceImpl(CargoDAO cargoDAO) {
        this.cargoDAO = cargoDAO;
    }

    @Override
    public Cargo findById(int cargoId) {
        return cargoDAO.findById(cargoId);
    }

    @Override
    public List<Cargo> findAll() {
        return cargoDAO.findAll();
    }

    @Override
    public Cargo updateCargo(Cargo cargo) {
        return cargoDAO.updateCargo(cargo);
    }

    @Override
    public void addCargo(Cargo cargo) {
        cargoDAO.addCargo(cargo);
    }

    @Override
    public void deleteCargoById(int cargoId) {
        cargoDAO.deleteCargoById(cargoId);
    }
}
