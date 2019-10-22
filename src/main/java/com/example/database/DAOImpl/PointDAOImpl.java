package com.example.database.DAOImpl;

import com.example.database.DAO.PointDAO;
import com.example.models.Point;
import com.example.database.repositories.PointRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PointDAOImpl implements PointDAO {

    private final PointRepository pointRepository;

    public PointDAOImpl(PointRepository pointRepository) {
        this.pointRepository = pointRepository;
    }

    @Override
    public Point findById(int pointId) {
        return pointRepository.findById(pointId).get();
    }

    @Override
    public List<Point> findAll() {
        List<Point> points = new ArrayList<>();
        pointRepository.findAll().forEach(points::add);

        return points;
    }

    @Override
    public Point updatePoint(Point point) {
        return pointRepository.save(point);
    }

    @Override
    public void addPoint(Point point) {
        pointRepository.save(point);
    }

    @Override
    public void deletePointById(int pointId) {
        pointRepository.deleteById(pointId);
    }
}
