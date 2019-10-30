package com.example.controllers;

import com.example.models.DriverDto;
import com.example.services.DriverService;
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
    public DriverDto findById(@PathVariable int driverDtoId) {
        return driverService.findById(driverDtoId);
    }

    @GetMapping("/free")
    public List<DriverDto> getFreeDrivers() {
        return driverService.getFreeDrivers();
    }

    @PutMapping
    public DriverDto updateDriver(@RequestBody DriverDto driverDto) {
        return driverService.updateDriver(driverDto);
    }

    @PostMapping
    public void addDriver(@RequestBody DriverDto driverDto) {
        driverService.addDriver(driverDto);
    }
}
