package com.example.controller;

import com.example.controller.exceptions.RegistrationNumberExistsException;
import com.example.controller.exceptions.TruckNotFoundException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.TruckDto;
import com.example.services.serviceImpl.validation.exception.TruckValidationException;
import com.example.services.TruckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trucks")
public class TruckController {

    private TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public List<TruckDto> findAll(@RequestParam("page") int page, @RequestParam("size") int pageSize) {
        return truckService.findAll(page, pageSize);
    }

    @GetMapping("/search/{text}")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<TruckDto> findAll(@PathVariable String text) {
        return truckService.getTrucksBySearch(text);
    }

    @GetMapping("free/{weight}")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<TruckDto> getFreeTrucks(@PathVariable double weight) {
        return truckService.getFreeTrucks(weight);
    }

    @GetMapping("/{truckDtoId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public TruckDto findById(@PathVariable Long truckDtoId) {
        return truckService.findById(truckDtoId);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateTruck(@RequestBody TruckDto truckDto) {
        return truckService.updateTruck(truckDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addTruck(@RequestBody TruckDto truckDto) {
        return truckService.addTruck(truckDto);
    }

    @ExceptionHandler({RegistrationNumberExistsException.class, TruckValidationException.class})
    public ResponseEntity handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleException(TruckNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
