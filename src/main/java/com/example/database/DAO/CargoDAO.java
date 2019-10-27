package com.example.database.DAO;

import com.example.database.models.Cargo;
import java.util.List;

public interface CargoDAO {

    Cargo findById(int cargoId);
    List<Cargo> findAll();
    Cargo updateCargo(Cargo cargo);
    void addCargo(Cargo cargo);
}
