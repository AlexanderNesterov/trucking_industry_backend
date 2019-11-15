package com.example.services;

import com.example.models.UserDto;

import java.util.List;

public interface UserService {
    
    UserDto findById(Long userDtoId);
    List<UserDto> findAll();
    UserDto updateUser(UserDto user);
    UserDto addUser(UserDto user);
}
