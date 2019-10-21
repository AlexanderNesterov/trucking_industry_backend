package com.example.controllers;

import com.example.models.Driver;
import com.example.services.DriverService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public List<Driver> findAll() {
        return driverService.findAll();
    }

    @GetMapping("/{driverId}")
    public Driver findById(@PathVariable int driverId) {
        return driverService.findById(driverId);
    }

    @PutMapping
    public Driver updateDriver(@RequestBody Driver driver) {
        return driverService.updateDriver(driver);
    }

    @PostMapping
    public void addDriver(@RequestBody Driver driver) {
        driverService.addDriver(driver);
    }

    @DeleteMapping("/{driverId}")
    public void deleteDriver(@PathVariable int driverId) {
        driverService.deleteDriverById(driverId);
    }

}
