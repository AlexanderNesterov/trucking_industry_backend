package com.example.services;

import com.example.services.models.FullInfoManagerDto;
import com.example.services.models.SimpleManagerDto;

import javax.validation.Valid;
import java.util.List;

public interface ManagerService {

    SimpleManagerDto findById(Long managerId);
    List<SimpleManagerDto> getManagers(String text, int page, int pageSize);
    boolean updateManager(@Valid SimpleManagerDto user);
    boolean addManager(@Valid FullInfoManagerDto user);
    boolean blockAccount(Long userId, Long managerId);
}
