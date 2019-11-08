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
        
        StringBuilder sb = checkDrivers(existsDrivers, driverDto, true);

        if (sb != null && sb.length() != 0) {
            throw new DriverExistsException(sb.toString());
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

        StringBuilder sb = checkDrivers(existsDrivers, driverDto, false);

        if (sb != null && sb.length() != 0) {
            throw new DriverExistsException(sb.toString());
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
    
    private StringBuilder checkDrivers(List<DriverDto> existsDrivers, DriverDto savedDriver, boolean isUpdate) {
        if (existsDrivers.size() == 0) {
            return null;
        }
        
        StringBuilder sb = new StringBuilder();

        for (DriverDto existDriver : existsDrivers) {
            if (isUpdate && savedDriver.getId() == existDriver.getId() &&
                    savedDriver.getUserDto().getId() == existDriver.getUserDto().getId()) {
                continue;
            }

            if (existDriver.getUserDto().getLogin().equals(savedDriver.getUserDto().getLogin())) {
                sb.append("Driver with login: ");
                sb.append(savedDriver.getUserDto().getLogin());
                sb.append(" already exist, ");
            }

            if (existDriver.getDriverLicense().equals(savedDriver.getDriverLicense())) {
                sb.append("Driver with driver license: ");
                sb.append(savedDriver.getDriverLicense());
                sb.append(" already exist, ");
            }
        }

        if (sb.length() != 0) {
            sb.delete(sb.length() - 2, sb.length());
        }

        return sb;
    }
}
