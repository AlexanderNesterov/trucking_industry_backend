package com.example.controllers;

import com.example.models.UserDto;
import com.example.services.ManagerService;
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
}
