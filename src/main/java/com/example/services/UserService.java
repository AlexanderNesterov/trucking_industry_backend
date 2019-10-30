package com.example.services;

import com.example.models.UserDto;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    
    UserDto findById(int userDtoId);
    List<UserDto> findAll();
    UserDto updateUser(@Valid UserDto user);
    UserDto addUser(@Valid UserDto user);
}
