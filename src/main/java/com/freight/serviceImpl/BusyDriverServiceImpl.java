package com.freight.serviceImpl;

import com.freight.DAO.BusyDriverDAO;
import com.freight.models.BusyDriver;
import com.freight.services.BusyDriverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusyDriverServiceImpl implements BusyDriverService {

    private final BusyDriverDAO busyDriverDAO;

    public BusyDriverServiceImpl(BusyDriverDAO busyDriverDAO) {
        this.busyDriverDAO = busyDriverDAO;
    }

    @Override
    public BusyDriver findById(int busyDriverId) {
        return busyDriverDAO.findById(busyDriverId);
    }

    @Override
    public List<BusyDriver> findAll() {
        return busyDriverDAO.findAll();
    }

    @Override
    public BusyDriver updateBusyDriver(BusyDriver busyDriver) {
        return busyDriverDAO.updateBusyDriver(busyDriver);
    }

    @Override
    public void addBusyDriver(BusyDriver busyDriver) {
        busyDriverDAO.addBusyDriver(busyDriver);
    }

    @Override
    public void deleteBusyDriverById(int busyDriverId) {
        busyDriverDAO.deleteBusyDriverById(busyDriverId);
    }
}
