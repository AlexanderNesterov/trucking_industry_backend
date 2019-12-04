package com.example.services;

import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleManagerDto;
import com.example.services.models.SimpleUserDto;

import java.util.List;

public interface ManagerService {
    
    FullInfoManagerDto findById(Long managerId);
    List<SimpleManagerDto> findAll();
    boolean updateManager(FullInfoManagerDto user);
    boolean addManager(FullInfoManagerDto user);
}
