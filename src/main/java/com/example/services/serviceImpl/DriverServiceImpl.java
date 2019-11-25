package com.example.services.serviceImpl;

import com.example.controller.exceptions.DriverExistsException;
import com.example.controller.exceptions.DriverNotFoundException;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.services.models.DriverDto;
import com.example.services.serviceImpl.validation.DriverValidator;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverServiceImpl implements DriverService {

    private DriverRepository driverRepository;
    private DriverMapper driverMapper;

    public DriverServiceImpl(DriverMapper driverMapper) {
        this.driverMapper = driverMapper;
    }

    @Autowired
    public DriverServiceImpl(DriverRepository driverRepository, DriverMapper driverMapper) {
        this.driverRepository = driverRepository;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverDto findById(Long driverDtoId) {
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
    public boolean updateDriver(DriverDto driverDto) {
        DriverDto sameDriverDto = findById(driverDto.getId());
        driverDto.getUser().setLogin(sameDriverDto.getUser().getLogin());

        checkSavingDriver(driverDto, true);

        driverDto.getUser().setRole(sameDriverDto.getUser().getRole());
        driverDto.getUser().setPassword(sameDriverDto.getUser().getPassword());
        driverDto.setStatus(sameDriverDto.getStatus());
        driverRepository.save(driverMapper.fromDto(driverDto));
        return true;
    }

    @Override
    public boolean addDriver(DriverDto driverDto) {
        DriverValidator.validate(driverDto, false);
        checkSavingDriver(driverDto, false);

        driverDto.setId(null);
        driverDto.getUser().setId(null);
        driverDto.getUser().setRole(Role.DRIVER);
        driverDto.setStatus(DriverStatus.REST);
        driverRepository.save(driverMapper.fromDto(driverDto));
        return true;
    }

    @Override
    public List<DriverDto> getFreeDrivers() {
        return driverMapper.toListDto(driverRepository.getFreeDrivers());
    }

    @Override
    public DriverDto getFreeDriver(Long driverId) {
        return driverMapper.toDto(driverRepository.getFreeDriver(driverId));
    }

    private Driver getDriverByLogin(String driverLogin) {
        return driverRepository.getDriverByLogin(driverLogin);
    }

    private Driver getDriverByDriverLicense(String driverLicense) {
        return driverRepository.getDriverByDriverLicense(driverLicense);
    }

    private void checkSavingDriver(DriverDto savingDriver, boolean isUpdate) {
        StringBuilder exception = new StringBuilder();

        if (isUpdate && savingDriver.getUser().getId() == 0) {
            exception.append("User id cannot be equals or less than 0: ");
            exception.append(savingDriver.getUser().getId());
            throw new DriverExistsException(exception.toString());
        }

        if (!isUpdate) {
            Driver existDriverLogin = getDriverByLogin(savingDriver.getUser().getLogin());
            if (existDriverLogin != null) {
                exception.append("Driver with login: ");
                exception.append(savingDriver.getUser().getLogin());
                exception.append(" already exist");
                throw new DriverExistsException(exception.toString());
            }
        }

        Driver existDriverDriverLicense = getDriverByDriverLicense(savingDriver.getDriverLicense());
        if (existDriverDriverLicense == null ||
                (isUpdate && existDriverDriverLicense.getId().equals(savingDriver.getId()))) {
            return;
        }

        exception.append("Driver with driver license: ");
        exception.append(savingDriver.getDriverLicense());
        exception.append(" already exist");
        throw new DriverExistsException(exception.toString());
    }
}
