package com.example.controllers;

import com.example.controllers.exceptions.DriverExistsException;
import com.example.controllers.exceptions.DriverNotFoundException;
import com.example.controllers.response.ErrorResponse;
import com.example.models.DriverDto;
import com.example.serviceImpl.validation.exception.UserValidationException;
import com.example.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/drivers")
public class DriverController {

    private DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<DriverDto> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{driverDtoId}")
    public DriverDto findById(@PathVariable Long driverDtoId) {
        return driverService.findById(driverDtoId);
    }

    @GetMapping("/free")
    public List<DriverDto> getFreeDrivers() {
        return driverService.getFreeDrivers();
    }

    @PutMapping
    public boolean updateDriver(@RequestBody DriverDto driverDto) {
        return driverService.updateDriver(driverDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addDriver(@RequestBody DriverDto driverDto) {
        return driverService.addDriver(driverDto);
    }

    @ExceptionHandler({DriverExistsException.class, UserValidationException.class})
    public ResponseEntity handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleException(DriverNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
