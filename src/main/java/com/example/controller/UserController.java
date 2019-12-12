package com.example.controller;

import com.example.services.UserService;
import com.example.services.models.ChangePasswordDto;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
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

/*    @PutMapping("/block-driver/{userId}/{driverId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean blockDriverAccount(@PathVariable Long userId, @PathVariable Long driverId) {
        return userService.blockDriverAccount(userId, driverId);
    }

    @PutMapping("/block-manager/{userId}/{managerId}")
    @PreAuthorize("#managerId != authentication.principal.managerId")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean blockManagerAccount(@PathVariable Long userId, @PathVariable Long managerId) {
        return userService.blockManagerAccount(userId, managerId);
    }*/

    @PutMapping("/unlock/{userId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean unlockAccount(@PathVariable Long userId) {
        return userService.unlockAccount(userId);
    }

    @PostMapping("/admin-password")
    @PreAuthorize("hasRole('ROLE_ADMIN') && (#passwordDto.login == authentication.principal.subject)")
    public boolean changeAdminPassword(@RequestBody ChangePasswordDto passwordDto) {

        return userService.changePassword(passwordDto);
    }

    @PostMapping("/driver-password/{login}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean changeDriverPassword(@RequestBody ChangePasswordDto passwordDto,
                                        @PathVariable String login) {
        return userService.changePassword(passwordDto);
    }
}
