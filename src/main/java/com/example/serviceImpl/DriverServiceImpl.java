package com.example.serviceImpl;

import com.example.controllers.exceptions.DriverExistsException;
import com.example.controllers.exceptions.DriverNotFoundException;
import com.example.database.models.Driver;
import com.example.database.models.commons.DriverStatus;
import com.example.database.models.commons.Role;
import com.example.database.repositories.DriverRepository;
import com.example.models.DriverDto;
import com.example.serviceImpl.validation.DriverValidator;
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
    public boolean updateDriver(DriverDto driverDto) {
        DriverDto sameDriverDto = findById(driverDto.getId());
        driverDto.getUserDto().setLogin(sameDriverDto.getUserDto().getLogin());

        checkSavingDriver(driverDto, true);

        driverDto.getUserDto().setRole(sameDriverDto.getUserDto().getRole());
        driverDto.getUserDto().setPassword(sameDriverDto.getUserDto().getPassword());
        driverDto.setStatus(sameDriverDto.getStatus());
        driverRepository.save(driverMapper.fromDto(driverDto));

        return true;
    }

    @Override
    public boolean addDriver(DriverDto driverDto) {
        DriverValidator.validate(driverDto, false);
        checkSavingDriver(driverDto, false);

        driverDto.setId(0);
        driverDto.getUserDto().setId(0);
        driverDto.getUserDto().setRole(Role.DRIVER);
        driverDto.setStatus(DriverStatus.REST);
        //driverRepository.save(driverMapper.fromDto(driverDto));

        return true;
    }

    @Override
    public List<DriverDto> getFreeDrivers() {
        return driverMapper.toListDto(driverRepository.getFreeDrivers());
    }

    private DriverDto getDriverByLogin(String driverLogin) {
        return driverMapper.toDto(driverRepository.getDriverByLogin(driverLogin));
    }

    private DriverDto getDriverByDriverLicense(String driverLicense) {
        return driverMapper.toDto(driverRepository.getDriverByDriverLicense(driverLicense));
    }

    private void checkSavingDriver(DriverDto savingDriver, boolean isUpdate) {
        StringBuilder exception = new StringBuilder();

        //Добавить тест
        if (isUpdate && savingDriver.getUserDto().getId() == 0) {
            exception.append("User id cannot be equals or less than 0: ");
            exception.append(savingDriver.getUserDto().getId());
            throw new DriverExistsException(exception.toString());
        }

        if (!isUpdate) {
            DriverDto existDriverLogin = getDriverByLogin(savingDriver.getUserDto().getLogin());
            if (existDriverLogin != null) {
                exception.append("Driver with login: ");
                exception.append(savingDriver.getUserDto().getLogin());
                exception.append(" already exist");
                throw new DriverExistsException(exception.toString());
            }
        }

        DriverDto existDriverDriverLicense = getDriverByDriverLicense(savingDriver.getDriverLicense());
        if (existDriverDriverLicense == null || (isUpdate && existDriverDriverLicense.getId() == savingDriver.getId())) {
            return;
        }

        exception.append("Driver with driver license: ");
        exception.append(savingDriver.getDriverLicense());
        exception.append(" already exist");
        throw new DriverExistsException(exception.toString());
    }
}
