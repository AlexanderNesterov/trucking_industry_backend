package com.example.controller;

import com.example.controller.exceptions.CargoNotFoundException;
import com.example.controller.exceptions.ChangeCargoStatusException;
import com.example.controller.exceptions.SavingCargoException;
import com.example.controller.response.ErrorResponse;
import com.example.services.models.OrderDto;
import com.example.services.serviceImpl.validation.exception.CargoValidationException;
import com.example.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public List<OrderDto> findAll(@RequestParam("page") int page, @RequestParam("size") int pageSize) {
        return orderService.findAll(page, pageSize);
    }

    @GetMapping("/search/{text}")
    @RolesAllowed({"ROLE_ADMIN"})
    public List<OrderDto> getOrdersBySearch(@PathVariable String text) {
        return orderService.getOrdersBySearch(text);
    }

    @GetMapping("/{orderId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public OrderDto findById(@PathVariable Long orderId) {
        return orderService.findById(orderId);
    }

    @GetMapping("/for-driver/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public OrderDto getOrderByDriverId(@PathVariable Long driverId) {
        return orderService.getOrderByDriverId(driverId);
    }

    @PutMapping("/set-accept-status/{orderId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setAcceptStatus(@PathVariable Long orderId, @PathVariable Long driverId) {
        return orderService.setAcceptStatus(orderId, driverId);
    }

    @PutMapping("/set-refuse-status/{orderId}/{driverId}")
    @PreAuthorize("#driverId == authentication.principal.driverId")
    @RolesAllowed({"ROLE_DRIVER"})
    public boolean setRefuseStatus(@PathVariable Long orderId, @PathVariable Long driverId) {
        return orderService.setRefuseStatus(orderId, driverId);
    }

    @PutMapping("/set-cancel-status/{orderId}")
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean setCancelStatus(@PathVariable Long orderId) {
        return orderService.setCanceledStatus(orderId);
    }

    @PutMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean updateOrder(@RequestBody OrderDto orderDto) {
        return orderService.updateOrder(orderDto);
    }

    @PostMapping
    @RolesAllowed({"ROLE_ADMIN"})
    public boolean addOrder(@RequestBody OrderDto orderDto) {
        return orderService.addOrder(orderDto);
    }

    @ExceptionHandler
    public ResponseEntity handleException(CargoNotFoundException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SavingCargoException.class, ChangeCargoStatusException.class,
            CargoValidationException.class})
    public ResponseEntity handleException(RuntimeException exc) {
        ErrorResponse error = new ErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
