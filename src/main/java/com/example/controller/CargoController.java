package com.example.controller;

import com.example.controller.exceptions.ChangeOrderStatusException;
import com.example.controller.response.ErrorResponse;
import com.example.services.CargoService;
import com.example.services.models.CargoDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trucking-industry/cargo")
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

    @GetMapping("/{orderId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<CargoDto> getCargoListByOrderId(@PathVariable Long orderId) {
        return cargoService.getCargoListByOrderId(orderId);
    }

    @ExceptionHandler(ChangeOrderStatusException.class)
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
