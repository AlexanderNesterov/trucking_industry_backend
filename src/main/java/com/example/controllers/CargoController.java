package com.example.controllers;

import com.example.models.CargoDto;
import com.example.services.CargoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cargo")
public class CargoController {

    private CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @GetMapping
    public List<CargoDto> findAll() {
        return cargoService.findAll();
    }

    @GetMapping("/{cargoId}")
    public CargoDto findById(@PathVariable int cargoId) {
        return cargoService.findById(cargoId);
    }

    @GetMapping("/for_driver/{driverId}")
    public CargoDto getCargoByDriverId(@PathVariable int driverId) {
        return cargoService.getCargoByDriverId(driverId);
    }

    @PutMapping("/set_accept_status/{cargoId}/{driverId}")
    public void setAcceptStatus(@PathVariable int cargoId, @PathVariable int driverId) {
        cargoService.setAcceptStatus(cargoId, driverId);
    }

    @PutMapping("/set_refuse_status/{cargoId}/{driverId}")
    public void setRefuseStatus(@PathVariable int cargoId, @PathVariable int driverId) {
        cargoService.setRefuseStatus(cargoId, driverId);
    }

    @PutMapping
    public CargoDto updateCargo(@RequestBody CargoDto cargoDto) {
        return cargoService.updateCargo(cargoDto);
    }

    @PostMapping
    public void addCargo(@RequestBody CargoDto cargoDto) {
        cargoService.addCargo(cargoDto);
    }
}
