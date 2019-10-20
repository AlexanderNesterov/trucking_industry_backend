package com.freight.controllers;

import com.freight.models.CitiesDistance;
import com.freight.services.CitiesDistanceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities_distances")
public class CitiesDistanceController {
    
    private final CitiesDistanceService citiesDistanceService;

    public CitiesDistanceController(CitiesDistanceService citiesDistanceService) {
        this.citiesDistanceService = citiesDistanceService;
    }

    @GetMapping
    public List<CitiesDistance> findAll() {
        return citiesDistanceService.findAll();
    }

    @GetMapping("/{citiesDistanceId}")
    public CitiesDistance findById(@PathVariable int citiesDistanceId) {
        return citiesDistanceService.findById(citiesDistanceId);
    }

    @PutMapping
    public CitiesDistance updateCitiesDistance(@RequestBody CitiesDistance citiesDistance) {
        return citiesDistanceService.updateCitiesDistance(citiesDistance);
    }

    @PostMapping
    public void addCCitiesDistance(@RequestBody CitiesDistance citiesDistance) {
        citiesDistanceService.addCitiesDistance(citiesDistance);
    }

    @DeleteMapping("/{citiesDistanceId}")
    public void deleteCitiesDistance(@PathVariable int citiesDistanceId) {
        citiesDistanceService.deleteCitiesDistanceById(citiesDistanceId);
    }
}
