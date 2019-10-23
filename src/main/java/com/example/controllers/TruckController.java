package com.example.controllers;

import com.example.models.TruckDto;
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
    public List<TruckDto> findAll() {
        return truckService.findAll();
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
    public void addTruck(@RequestBody TruckDto truckDto) {
        truckService.addTruck(truckDto);
    }

    @DeleteMapping("/{truckDtoId}")
    public void deleteTruck(@PathVariable int truckDtoId) {
        truckService.deleteTruckById(truckDtoId);
    }
}
