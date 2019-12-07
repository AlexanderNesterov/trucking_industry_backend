package com.example.controller;

import com.example.services.CityService;
import com.example.services.models.CityDto;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public List<CityDto> findAll() {
        return cityService.findAll();
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean addCity(@RequestBody CityDto city) {
        return cityService.addCity(city);
    }
}
