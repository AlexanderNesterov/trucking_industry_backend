package com.freight.controllers;

import com.freight.models.Cargo;
import com.freight.services.CargoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cargo")
public class CargoController {

    private CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public List<Cargo> findAll() {
        return cargoService.findAll();
    }

    @GetMapping("/{cargoId}")
    public Cargo findById(@PathVariable int cargoId) {
        return cargoService.findById(cargoId);
    }

    @PutMapping
    public Cargo updateCargo(@RequestBody Cargo cargo) {
        return cargoService.updateCargo(cargo);
    }

    @PostMapping
    public void addCargo(@RequestBody Cargo cargo) {
        cargoService.addCargo(cargo);
    }

    @DeleteMapping("/{cargoId}")
    public void deleteCargo(@PathVariable int cargoId) {
        cargoService.deleteCargoById(cargoId);
    }
}
