package com.example.controller;

import com.example.services.CargoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@CrossOrigin
@RequestMapping("/cargo")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PutMapping("/set-deliver-status/{cargoId}/{orderId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setDeliverStatus(@PathVariable Long cargoId, @PathVariable Long orderId,
                                    @PathVariable Long driverId) {
        return cargoService.setDeliverStatus(cargoId, orderId, driverId);
    }
}
