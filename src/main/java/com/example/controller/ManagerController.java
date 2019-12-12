package com.example.controller;

import com.example.controller.exceptions.ManagerNotFoundException;
import com.example.controller.exceptions.SavingManagerException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;
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

    @GetMapping("/search")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<SimpleManagerDto> getManagersBySearch(@RequestParam("text") String text,
                                                      @RequestParam("page") int page,
                                                      @RequestParam("size") int pageSize) {
        return managerService.getManagers(text, page, pageSize);
    }

    @GetMapping("/{managerId}")
    @PreAuthorize("#managerId == authentication.principal.managerId")
    @RolesAllowed({"ROLE_ADMIN"})
    public SimpleManagerDto findById(@PathVariable Long managerId) {
        return managerService.findById(managerId);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateManager(@RequestBody SimpleManagerDto managerDto) {
        return managerService.updateManager(managerDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean addManager(@RequestBody FullInfoManagerDto managerDto) {
        return managerService.addManager(managerDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(SavingManagerException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ManagerNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
