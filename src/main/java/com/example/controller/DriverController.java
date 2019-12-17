package com.example.controller;

import com.example.controller.exceptions.DriverNotFoundException;
import com.example.controller.exceptions.SavingDriverException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.FullInfoDriverDto;
import com.example.services.models.SimpleDriverDto;
import com.example.services.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/trucking-industry/drivers")
public class DriverController {

    private DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping("/check")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean checkDriverLicense(@RequestParam("driver-license") String driverLicense,
                                      @RequestParam("driverId") Long driverId) {
        return driverService.isDriverLicenseExists(driverLicense, driverId);
    }

    @GetMapping("/{driverId}")
    @PreAuthorize("(hasRole('ROLE_DRIVER') && #driverId == authentication.principal.driverId) || hasRole('ROLE_ADMIN')")
    public SimpleDriverDto findById(@PathVariable Long driverId) {
        return driverService.findById(driverId);
    }

    @GetMapping("/search")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<SimpleDriverDto> getDriversBySearch(@RequestParam("text") String text,
                                                    @RequestParam("page") int page,
                                                    @RequestParam("size") int pageSize) {
        return driverService.getDrivers(text, page, pageSize);
    }

    @GetMapping("/free")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<SimpleDriverDto> getFreeDrivers(@RequestParam String text,
                                                @RequestParam int page,
                                                @RequestParam int size) {
        return driverService.getFreeDrivers(text, page, size);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateDriver(@Valid @RequestBody SimpleDriverDto driverDto) {
        return driverService.updateDriver(driverDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.CREATED)
    public boolean addDriver(@RequestBody FullInfoDriverDto driverDto) {
        return driverService.addDriver(driverDto);
    }

    @PutMapping("/block/{userId}/{driverId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean blockDriverAccount(@PathVariable Long userId, @PathVariable Long driverId) {
        return driverService.blockAccount(userId, driverId);
    }

    @ExceptionHandler({SavingDriverException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(DriverNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
