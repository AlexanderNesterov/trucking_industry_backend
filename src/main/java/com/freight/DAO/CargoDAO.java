package com.freight.DAO;

import com.freight.models.Cargo;
import java.util.List;

public interface CargoDAO {

    Cargo findById(int cargoId);
    List<Cargo> findAll();
    Cargo updateCargo(Cargo cargo);
    void addCargo(Cargo cargo);
    void deleteCargoById(int cargoId);
}
