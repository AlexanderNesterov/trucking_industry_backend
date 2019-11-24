package com.example.services;

import com.example.services.models.UserDto;

import java.util.List;

public interface ManagerService {
    
    UserDto findById(Long userDtoId);
    List<UserDto> findAll();
    boolean updateManager(UserDto user);
    boolean addManager(UserDto user);
}
