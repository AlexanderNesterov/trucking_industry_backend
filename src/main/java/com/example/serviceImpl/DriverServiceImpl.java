package com.example.serviceImpl;

import com.example.database.DAO.DriverDAO;
import com.example.models.DriverDto;
import com.example.services.DriverService;
import com.example.services.mappers.DriverMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class DriverServiceImpl implements DriverService {

    private final DriverDAO driverDAO;
    private final DriverMapper driverMapper;

    public DriverServiceImpl(DriverDAO driverDAO, DriverMapper driverMapper) {
        this.driverDAO = driverDAO;
        this.driverMapper = driverMapper;
    }

    @Override
    public DriverDto findById(int driverDtoId) {
        return driverMapper.toDto(driverDAO.findById(driverDtoId));
    }

    @Override
    public List<DriverDto> findAll() {
        return driverMapper.toListDto(driverDAO.findAll());
    }

    @Override
    public DriverDto updateDriver(@Valid DriverDto driverDto) {
        return driverMapper.toDto(driverDAO.updateDriver(driverMapper.fromDto(driverDto)));
    }

    @Override
    public void addDriver(@Valid DriverDto driverFullInfo) {
        driverDAO.addDriver(driverMapper.fromDto(driverFullInfo));
    }

    @Override
    public void deleteDriverById(int driverDtoId) {
        driverDAO.deleteDriverById(driverDtoId);
    }
}
