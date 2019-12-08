package com.example.controller;

import com.example.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/check")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean checkDriverLicense(@RequestParam("login") String login) {
        return userService.isLoginExists(login);
    }
}
