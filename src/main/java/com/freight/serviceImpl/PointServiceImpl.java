package com.freight.serviceImpl;

import com.freight.DAO.PointDAO;
import com.freight.models.Point;
import com.freight.services.PointService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointServiceImpl implements PointService {

    private final PointDAO pointDAO;

    public PointServiceImpl(PointDAO pointDAO) {
        this.pointDAO = pointDAO;
    }

    @Override
    public Point findById(int pointId) {
        return pointDAO.findById(pointId);
    }

    @Override
    public List<Point> findAll() {
        return pointDAO.findAll();
    }

    @Override
    public Point updatePoint(Point point) {
        return pointDAO.updatePoint(point);
    }

    @Override
    public void addPoint(Point point) {
        pointDAO.addPoint(point);
    }

    @Override
    public void deletePointById(int pointId) {
        pointDAO.deletePointById(pointId);
    }
}
