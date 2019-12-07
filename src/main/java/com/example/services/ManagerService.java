package com.example.services;

import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;

import javax.validation.Valid;
import java.util.List;

public interface ManagerService {

    FullInfoManagerDto findById(Long managerId);
    List<SimpleManagerDto> getManagers(String text, int page, int pageSize);
    boolean updateManager(@Valid FullInfoManagerDto user);
    boolean addManager(@Valid FullInfoManagerDto user);
}
