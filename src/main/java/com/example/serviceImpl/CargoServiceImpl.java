package com.example.serviceImpl;

import com.example.database.DAO.CargoDAO;
import com.example.models.CargoDto;
import com.example.services.CargoService;
import com.example.services.mappers.CargoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class CargoServiceImpl implements CargoService {

    private final CargoMapper cargoMapper;
    private final CargoDAO cargoDAO;

    public CargoServiceImpl(CargoMapper cargoMapper, CargoDAO cargoDAO) {
        this.cargoMapper = cargoMapper;
        this.cargoDAO = cargoDAO;
    }

    @Override
    public CargoDto findById(int cargoId) {
        return cargoMapper.toDto(cargoDAO.findById(cargoId));
    }

    @Override
    public List<CargoDto> findAll() {
        return cargoMapper.toListDto(cargoDAO.findAll());
    }

    @Override
    public CargoDto updateCargo(@Valid CargoDto cargoDto) {
        return cargoMapper.toDto(cargoDAO.updateCargo(cargoMapper.fromDto(cargoDto)));
    }

    @Override
    public void addCargo(@Valid CargoDto cargoDto) {
        cargoDAO.addCargo(cargoMapper.fromDto(cargoDto));
    }
}
