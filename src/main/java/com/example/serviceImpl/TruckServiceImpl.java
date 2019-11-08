package com.example.serviceImpl;

import com.example.controllers.exceptions.RegistrationNumberExistsException;
import com.example.controllers.exceptions.TruckNotFoundException;
import com.example.database.models.Truck;
import com.example.database.models.commons.TruckCondition;
import com.example.database.repositories.TruckRepository;
import com.example.models.TruckDto;
import com.example.services.TruckService;
import com.example.services.mappers.TruckMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;
    private final TruckMapper truckMapper;


    public TruckServiceImpl(TruckRepository truckRepository, TruckMapper truckMapper) {
        this.truckRepository = truckRepository;
        this.truckMapper = truckMapper;
    }

    @Override
    public TruckDto findById(int truckDtoId) {
        Optional<Truck> optional = truckRepository.findById(truckDtoId);

        if (optional.isPresent()) {
            return truckMapper.toDto(optional.get());
        } else {
            throw new TruckNotFoundException("Truck with id= " + truckDtoId + " not found");
        }
    }

    @Override
    public List<TruckDto> findAll() {
        List<TruckDto> truckDtos = new ArrayList<>();
        truckRepository.findAll().forEach(truck ->
                truckDtos.add(truckMapper.toDto(truck)));
        return truckDtos;
    }

    @Override
    public boolean updateTruck(@Valid TruckDto truckDto) {
        TruckDto existTruck = truckMapper.toDto(
                truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber()));

        if (existTruck.getId() != truckDto.getId()) {
            throw new RegistrationNumberExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setCondition(existTruck.getCondition());
        truckRepository.save(truckMapper.fromDto(truckDto));
        return true;
    }

    @Override
    public boolean addTruck(@Valid TruckDto truckDto) {
        TruckDto existTruck = truckMapper.toDto(
                truckRepository.getTruckByRegistrationNumber(truckDto.getRegistrationNumber()));

        if (existTruck != null) {
            throw new RegistrationNumberExistsException("Truck with registration number: " +
                    truckDto.getRegistrationNumber() + " already exists");
        }

        truckDto.setId(0);
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        truckRepository.save(truckMapper.fromDto(truckDto));
        return true;
    }

    @Override
    public List<TruckDto> getFreeTrucks(double weight) {
        return truckMapper.toListDto(truckRepository.getFreeTrucks(weight));
    }
}
