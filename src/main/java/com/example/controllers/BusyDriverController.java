package com.example.controllers;

import com.example.models.BusyDriver;
import com.example.services.BusyDriverService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/busy_drivers")
public class BusyDriverController {

    private BusyDriverService busyDriverService;

    public BusyDriverController(BusyDriverService busyDriverService) {
        this.busyDriverService = busyDriverService;
    }

    @GetMapping
    public List<BusyDriver> findAll() {
        return busyDriverService.findAll();
    }

    @GetMapping("/{busyDriverId}")
    public BusyDriver findById(@PathVariable int busyDriverId) {
        return busyDriverService.findById(busyDriverId);
    }

    @PutMapping
    public BusyDriver updateBusyDriver(@RequestBody BusyDriver busyDriver) {
        return busyDriverService.updateBusyDriver(busyDriver);
    }

    @PostMapping
    public void addBusyDriver(@RequestBody BusyDriver busyDriver) {
        busyDriverService.addBusyDriver(busyDriver);
    }

    @DeleteMapping("/{busyDriverId}")
    public void deleteBusyDriver(@PathVariable int busyDriverId) {
        busyDriverService.deleteBusyDriverById(busyDriverId);
    }
}
