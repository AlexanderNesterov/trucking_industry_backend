package com.example.serviceImpl;

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
        return truckMapper.toDto(truckRepository.findById(truckDtoId).get());
    }

    @Override
    public List<TruckDto> findAll() {
        List<TruckDto> truckDtos = new ArrayList<>();
        truckRepository.findAll().forEach(truck -> truckDtos.add(truckMapper.toDto(truck)));
        return truckDtos;
    }

    @Override
    public TruckDto updateTruck(@Valid TruckDto truckDto) {
        return truckMapper.toDto(truckRepository.save(truckMapper.fromDto(truckDto)));
    }

    @Override
    public TruckDto addTruck(@Valid TruckDto truckDto) {
        truckDto.setCondition(TruckCondition.SERVICEABLE);
        return truckMapper.toDto(truckRepository.save(truckMapper.fromDto(truckDto)));
    }

    @Override
    public List<TruckDto> getFreeTrucks(double weight) {
        return truckMapper.toListDto(truckRepository.getFreeTrucks(weight));
    }
}
