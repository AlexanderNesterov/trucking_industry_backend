package com.example.controllers;

import com.example.models.TruckDto;
import com.example.services.TruckService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController()
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
    public TruckDto updateTruck(@RequestBody TruckDto truckDto) {
        return truckService.updateTruck(truckDto);
    }

    @PostMapping
    public TruckDto addTruck(@RequestBody TruckDto truckDto) {
        return truckService.addTruck(truckDto);
    }
}
