package com.example.controllers;

import com.example.models.Truck;
import com.example.services.TruckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/trucks")
public class TruckController {

    private TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    public List<Truck> findAll() {
        return truckService.findAll();
    }

    @GetMapping("/{truckId}")
    public Truck findById(@PathVariable int truckId) {
        return truckService.findById(truckId);
    }

    @PutMapping
    public Truck updateTruck(@RequestBody Truck truck) {
        return truckService.updateTruck(truck);
    }

    @PostMapping
    public void addTruck(@RequestBody Truck truck) {
        truckService.addTruck(truck);
    }

    @DeleteMapping("/{truckId}")
    public void deleteTruck(@PathVariable int truckId) {
        truckService.deleteTruckById(truckId);
    }
}
