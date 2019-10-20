package com.freight.DAO;

import com.freight.models.Point;

import java.util.List;

public interface PointDAO {

    Point findById(int pointId);
    List<Point> findAll();
    Point updatePoint(Point point);
    void addPoint(Point point);
    void deletePointById(int pointId);
}
