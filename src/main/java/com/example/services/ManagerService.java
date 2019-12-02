package com.example.services;

import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleUserDto;

import java.util.List;

public interface ManagerService {
    
    FullInfoUserDto findById(Long userDtoId);
    List<SimpleUserDto> findAll();
    boolean updateManager(FullInfoUserDto user);
    boolean addManager(FullInfoUserDto user);
}
