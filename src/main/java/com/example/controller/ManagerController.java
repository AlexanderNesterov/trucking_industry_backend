package com.example.controller;

import com.example.controller.exceptions.ManagerExistException;
import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.UserDto;
import com.example.services.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public List<UserDto> findAll() {
        return managerService.findAll();
    }

    @GetMapping("/{managerId}")
    @PreAuthorize("#managerId == authentication.principal.userId")
    @RolesAllowed({"ROLE_ADMIN"})
    public UserDto findById(@PathVariable Long managerId) {
        return managerService.findById(managerId);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateManager(@RequestBody UserDto userDto) {
        return managerService.updateManager(userDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean addManager(@RequestBody UserDto userDto) {
        return managerService.addManager(userDto);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ManagerExistException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleException(ManagerNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
