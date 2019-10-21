package com.example.controllers;

import com.example.models.City;
import com.example.services.CityService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public List<City> findAll() {
        return cityService.findAll();
    }

    @GetMapping("/{cityId}")
    public City findById(@PathVariable int cityId) {
        return cityService.findById(cityId);
    }

    @PutMapping
    public City updateCity(@RequestBody City city) {
        return cityService.updateCity(city);
    }

    @PostMapping
    public void addCity(@RequestBody City city) {
        cityService.addCity(city);
    }

    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable int cityId) {
        cityService.deleteCityById(cityId);
    }
}
