package com.example.database.DAOImpl;

import com.example.database.DAO.BusyDriverDAO;
import com.example.models.BusyDriver;
import com.example.database.repositories.BusyDriverRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class BusyDriverDAOImpl implements BusyDriverDAO {

    private final BusyDriverRepository busyDriverRepository;

    public BusyDriverDAOImpl(BusyDriverRepository busyDriverRepository) {
        this.busyDriverRepository = busyDriverRepository;
    }

    @Override
    public BusyDriver findById(int busyDriverId) {
        return busyDriverRepository.findById(busyDriverId).get();
    }

    @Override
    public List<BusyDriver> findAll() {
        List<BusyDriver> busyDrivers = new ArrayList<>();
        busyDriverRepository.findAll().forEach(busyDrivers::add);

        return busyDrivers;
    }

    @Override
    public BusyDriver updateBusyDriver(BusyDriver busyDriver) {
        return busyDriverRepository.save(busyDriver);
    }

    @Override
    public void addBusyDriver(BusyDriver busyDriver) {
        busyDriverRepository.save(busyDriver);
    }

    @Override
    public void deleteBusyDriverById(int busyDriverId) {
        busyDriverRepository.deleteById(busyDriverId);
    }
}
