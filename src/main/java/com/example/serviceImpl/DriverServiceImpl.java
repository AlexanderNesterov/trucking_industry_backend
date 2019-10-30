package com.example.serviceImpl;

import com.example.database.repositories.DriverRepository;
import com.example.models.DriverDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverDto findById(int driverDtoId) {
        return driverMapper.toDto(driverRepository.findById(driverDtoId).get());
    }

    @Override
    public List<DriverDto> findAll() {
        List<DriverDto> driverDtos = new ArrayList<>();
        driverRepository.findAll().forEach(driver -> driverDtos.add(driverMapper.toDto(driver)));
        return driverDtos;
    }

    @Override
    public DriverDto updateDriver(@Valid DriverDto driverDto) {
        return driverMapper.toDto(driverRepository.save(driverMapper.fromDto(driverDto)));
    }

    @Override
    public void addDriver(@Valid DriverDto driverFullInfo) {
        driverRepository.save(driverMapper.fromDto(driverFullInfo));
    }

    @Override
    public List<DriverDto> getFreeDrivers() {
        return driverMapper.toListDto(driverRepository.getFreeDrivers());
    }
}
