package com.example.controller;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.controller.exceptions.SavingCargoException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.CargoDto;
import com.example.services.serviceImpl.validation.exception.CargoValidationException;
import com.example.services.CargoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed({"ROLE_ADMIN"})
    public List<CargoDto> findAll() {
        return cargoService.findAll();
    }

    @GetMapping("/{cargoId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public CargoDto findById(@PathVariable Long cargoId) {
        return cargoService.findById(cargoId);
    }

    @GetMapping("/for-driver/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public CargoDto getCargoByDriverId(@PathVariable Long driverId) {
        return cargoService.getCargoByDriverId(driverId);
    }

    @PutMapping("/set-accept-status/{cargoId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setAcceptStatus(@PathVariable Long cargoId, @PathVariable Long driverId) {
        return cargoService.setAcceptStatus(cargoId, driverId);
    }

    @PutMapping("/set-refuse-status/{cargoId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setRefuseStatus(@PathVariable Long cargoId, @PathVariable Long driverId) {
        return cargoService.setRefuseStatus(cargoId, driverId);
    }

    @PutMapping("/set-deliver-status/{cargoId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setDeliverStatus(@PathVariable Long cargoId, @PathVariable Long driverId) {
        return cargoService.setDeliverStatus(cargoId, driverId);
    }

    @PutMapping("/set-cancel-status/{cargoId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean setCancelStatus(@PathVariable Long cargoId) {
        return cargoService.setCanceledStatus(cargoId);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateCargo(@RequestBody CargoDto cargoDto) {
        return cargoService.updateCargo(cargoDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
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
