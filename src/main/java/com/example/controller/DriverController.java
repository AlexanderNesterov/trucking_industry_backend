package com.example.controller;

import com.example.controller.exceptions.DriverExistsException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.DriverDto;
import com.example.services.serviceImpl.validation.exception.UserValidationException;
import com.example.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
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
    @RolesAllowed({"ROLE_ADMIN"})
    public List<DriverDto> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{driverId}")
    @PreAuthorize("(hasRole('ROLE_DRIVER') && #driverId == authentication.principal.driverId) || hasRole('ROLE_ADMIN')")
    public DriverDto findById(@PathVariable Long driverId) {
        return driverService.findById(driverId);
    }

    @GetMapping("/free")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<DriverDto> getFreeDrivers() {
        return driverService.getFreeDrivers();
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateDriver(@RequestBody DriverDto driverDto) {
        return driverService.updateDriver(driverDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
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
