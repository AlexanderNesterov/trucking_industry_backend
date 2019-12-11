package com.example.controller;

import com.example.controller.exceptions.TruckExistsException;
import com.example.controller.exceptions.TruckNotFoundException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.TruckDto;
import com.example.services.TruckService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trucks")
public class TruckController {

    private TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @GetMapping("/search")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<TruckDto> findAll(@RequestParam("text") String text,
                                  @RequestParam("page") int page,
                                  @RequestParam("size") int pageSize) {
        return truckService.getTrucks(text, page, pageSize);
    }

    @GetMapping("/check")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean checkRegistrationNumber(@RequestParam("registration-number") String registrationNumber,
                                           @RequestParam("truckId") Long truckId) {
        return truckService.isRegistrationNumberExists(registrationNumber, truckId);
    }

    @GetMapping("/check-to-update")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean canUpdateTruck(@RequestParam("truckId") Long truckId) {
        return truckService.canUpdateTruck(truckId);
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

    @PutMapping("/set-broken")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean setBrokenStatus(@RequestParam("truckId") Long truckId) {
        return truckService.setBrokenStatus(truckId);
    }

    @PutMapping("/set-serviceable")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean setServiceableStatus(@RequestParam("truckId") Long truckId) {
        return truckService.setServiceableStatus(truckId);
    }

    @ExceptionHandler({TruckExistsException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(TruckNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
