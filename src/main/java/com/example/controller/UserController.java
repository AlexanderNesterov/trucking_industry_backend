package com.example.controller;

import com.example.controller.exceptions.BlockAccountException;
import com.example.controller.exceptions.ChangePasswordException;
import com.example.controller.response.ErrorResponse;
import com.example.services.UserService;
import com.example.services.models.ChangePasswordDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public boolean changeDriverPassword(@RequestBody ChangePasswordDto passwordDto) {
        return userService.changePassword(passwordDto);
    }

    @ExceptionHandler({ChangePasswordException.class, BlockAccountException.class})
    public ResponseEntity<ErrorResponse> handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
