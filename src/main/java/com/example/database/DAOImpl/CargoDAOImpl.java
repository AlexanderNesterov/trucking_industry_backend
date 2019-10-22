package com.example.database.DAOImpl;

import com.example.database.DAO.CargoDAO;
import com.example.models.Cargo;
import com.example.database.repositories.CargoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CargoDAOImpl implements CargoDAO {

    private final CargoRepository cargoRepository;

    public CargoDAOImpl(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    @Override
    public Cargo findById(int cargoId) {
        return cargoRepository.findById(cargoId).get();
    }

    @Override
    public List<Cargo> findAll() {
        List<Cargo> cargos = new ArrayList<>();
        cargoRepository.findAll().forEach(cargos::add);

        return cargos;
    }

    @Override
    public Cargo updateCargo(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Override
    public void addCargo(Cargo cargo) {
        cargoRepository.save(cargo);
    }

    @Override
    public void deleteCargoById(int cargoId) {
        cargoRepository.deleteById(cargoId);
    }
}
