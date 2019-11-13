package com.example.controllers;

import com.example.controllers.exceptions.CargoNotFoundException;
import com.example.controllers.exceptions.ChangeCargoStatusException;
import com.example.controllers.exceptions.SavingCargoException;
import com.example.controllers.response.ErrorResponse;
import com.example.models.CargoDto;
import com.example.serviceImpl.validation.exception.CargoValidationException;
import com.example.services.CargoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public boolean setAcceptStatus(@PathVariable int cargoId, @PathVariable int driverId) {
        return cargoService.setAcceptStatus(cargoId, driverId);
    }

    @PutMapping("/set_refuse_status/{cargoId}/{driverId}")
    public boolean setRefuseStatus(@PathVariable int cargoId, @PathVariable int driverId) {
        return cargoService.setRefuseStatus(cargoId, driverId);
    }

    @PutMapping("/set_deliver_status/{cargoId}/{driverId}")
    public boolean setDeliverStatus(@PathVariable int cargoId, @PathVariable int driverId) {
        return cargoService.setDeliverStatus(cargoId, driverId);
    }

    @PutMapping
    public boolean updateCargo(@RequestBody CargoDto cargoDto) {
        return cargoService.updateCargo(cargoDto);
    }

    @PostMapping
    public boolean addCargo(@RequestBody CargoDto cargoDto) {
        return cargoService.addCargo(cargoDto);
    }

    @ExceptionHandler
    public ResponseEntity handleException(CargoNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SavingCargoException.class, ChangeCargoStatusException.class,
            CargoValidationException.class})
    public ResponseEntity handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
