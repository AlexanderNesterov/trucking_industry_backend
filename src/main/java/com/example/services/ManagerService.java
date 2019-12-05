package com.example.services;

import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.FullInfoUserDto;
import com.example.services.models.SimpleManagerDto;
import com.example.services.models.SimpleUserDto;

import javax.validation.Valid;
import java.util.List;

public interface ManagerService {
    
    FullInfoManagerDto findById(Long managerId);
    List<SimpleManagerDto> findAll(int page, int size);
    List<SimpleManagerDto> getManagersBySearch(String text);
    boolean updateManager(@Valid FullInfoManagerDto user);
    boolean addManager(@Valid FullInfoManagerDto user);
}
