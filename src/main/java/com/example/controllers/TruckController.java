package com.example.controllers;

import com.example.controllers.exceptions.RegistrationNumberExistsException;
import com.example.controllers.exceptions.TruckNotFoundException;
import com.example.controllers.response.ErrorResponse;
import com.example.models.TruckDto;
import com.example.services.TruckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trucks")
public class TruckController {

    private TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    public List<TruckDto> findAll() {
        return truckService.findAll();
    }

    @GetMapping("free/{weight}")
    public List<TruckDto> getFreeTrucks(@PathVariable double weight) {
        return truckService.getFreeTrucks(weight);
    }

    @GetMapping("/{truckDtoId}")
    public TruckDto findById(@PathVariable int truckDtoId) {
        return truckService.findById(truckDtoId);
    }

    @PutMapping
    public boolean updateTruck(@RequestBody TruckDto truckDto) {
        return truckService.updateTruck(truckDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addTruck(@RequestBody TruckDto truckDto) {
        return truckService.addTruck(truckDto);
    }

    @ExceptionHandler
    public ResponseEntity handleException(RegistrationNumberExistsException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleException(TruckNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
