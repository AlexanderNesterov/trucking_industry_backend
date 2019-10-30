package com.example.serviceImpl;

import com.example.database.repositories.CargoRepository;
import com.example.models.CargoDto;
import com.example.services.CargoService;
import com.example.services.mappers.CargoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class CargoServiceImpl implements CargoService {

    private final CargoMapper cargoMapper;
    private final CargoRepository cargoRepository;

    public CargoServiceImpl(CargoMapper cargoMapper, CargoRepository cargoRepository) {
        this.cargoMapper = cargoMapper;
        this.cargoRepository = cargoRepository;
    }

    @Override
    public CargoDto findById(int cargoId) {
        return cargoMapper.toDto(cargoRepository.findById(cargoId).get());
    }

    @Override
    public List<CargoDto> findAll() {
        List<CargoDto> cargoDtos = new ArrayList<>();
        cargoRepository.findAll().forEach(driver -> cargoDtos.add(cargoMapper.toDto(driver)));
        return cargoDtos;
    }

    @Override
    public CargoDto updateCargo(@Valid CargoDto cargoDto) {
        return cargoMapper.toDto(cargoRepository.save(cargoMapper.fromDto(cargoDto)));
    }

    @Override
    public void addCargo(@Valid CargoDto cargoDto) {
        cargoRepository.save(cargoMapper.fromDto(cargoDto));
    }
}
