package com.example.controllers;

import com.example.controllers.exceptions.ManagerExistException;
import com.example.controllers.exceptions.ManagerNotFoundException;
import com.example.controllers.response.ErrorResponse;
import com.example.models.UserDto;
import com.example.services.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService userService;

    public ManagerController(ManagerService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{managerId}")
    public UserDto findById(@PathVariable Long managerId) {
        return userService.findById(managerId);
    }

    @PutMapping
    public boolean updateManager(@RequestBody UserDto userDto) {
        return userService.updateManager(userDto);
    }

    @PostMapping
    public boolean addManager(@RequestBody UserDto userDto) {
        return userService.addManager(userDto);
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
