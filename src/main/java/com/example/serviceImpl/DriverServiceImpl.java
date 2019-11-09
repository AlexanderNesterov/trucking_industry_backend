package com.example.serviceImpl;

import com.example.controllers.exceptions.DriverExistsException;
import com.example.controllers.exceptions.DriverNotFoundException;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.models.DriverDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<Driver> driver = driverRepository.findById(driverDtoId);

        if (driver.isPresent()) {
            return driverMapper.toDto(driver.get());
        } else {
            throw new DriverNotFoundException("Driver with id: " + driverDtoId + " not found");
        }
    }

    @Override
    public List<DriverDto> findAll() {
        List<DriverDto> driverDtos = new ArrayList<>();
        driverRepository.findAll().forEach(driver ->
                driverDtos.add(driverMapper.toDto(driver)));
        return driverDtos;
    }

    @Override
    public boolean updateDriver(@Valid DriverDto driverDto) {
        List<DriverDto> existsDrivers = driverMapper.toListDto(
                driverRepository.getDriversByLoginAndDriverLicense(
                        driverDto.getUserDto().getLogin(), driverDto.getDriverLicense()));
        
        StringBuilder exception = checkSavingDriver(existsDrivers, driverDto, true);

        if (exception != null && exception.length() != 0) {
            throw new DriverExistsException(exception.toString());
        }

        driverDto.getUserDto().setRole(existsDrivers.get(0).getUserDto().getRole());
        driverDto.getUserDto().setPassword(existsDrivers.get(0).getUserDto().getPassword());
        driverDto.setStatus(existsDrivers.get(0).getStatus());
        driverRepository.save(driverMapper.fromDto(driverDto));
        return true;
    }

    @Override
    public boolean addDriver(@Valid DriverDto driverDto) {
        List<DriverDto> existsDrivers = driverMapper.toListDto(
                driverRepository.getDriversByLoginAndDriverLicense(
                        driverDto.getUserDto().getLogin(), driverDto.getDriverLicense()));

        StringBuilder exception = checkSavingDriver(existsDrivers, driverDto, false);

        if (exception != null && exception.length() != 0) {
            throw new DriverExistsException(exception.toString());
        }

        driverDto.setId(0);
        driverDto.getUserDto().setId(0);
        driverDto.getUserDto().setRole(Role.DRIVER);
        driverDto.setStatus(DriverStatus.REST);
        driverRepository.save(driverMapper.fromDto(driverDto));

        return true;
    }

    @Override
    public List<DriverDto> getFreeDrivers() {
        return driverMapper.toListDto(driverRepository.getFreeDrivers());
    }
    
    private StringBuilder checkSavingDriver(List<DriverDto> existsDrivers, DriverDto savingDriver, boolean isUpdate) {
        if (existsDrivers.size() == 0) {
            return null;
        }
        
        StringBuilder exception = new StringBuilder();

        for (DriverDto existDriver : existsDrivers) {
            if (isUpdate && savingDriver.getId() == existDriver.getId() &&
                    savingDriver.getUserDto().getId() == existDriver.getUserDto().getId()) {
                continue;
            }

            if (existDriver.getUserDto().getLogin().equals(savingDriver.getUserDto().getLogin())) {
                exception.append("Driver with login: ");
                exception.append(savingDriver.getUserDto().getLogin());
                exception.append(" already exist, ");
            }

            if (existDriver.getDriverLicense().equals(savingDriver.getDriverLicense())) {
                exception.append("Driver with driver license: ");
                exception.append(savingDriver.getDriverLicense());
                exception.append(" already exist, ");
            }
        }

        if (exception.length() != 0) {
            exception.delete(exception.length() - 2, exception.length());
        }

        return exception;
    }
}
