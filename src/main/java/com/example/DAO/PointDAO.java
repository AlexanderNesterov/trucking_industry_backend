package com.example.DAO;

import com.example.models.Point;

import java.util.List;

public interface PointDAO {

    Point findById(int pointId);
    List<Point> findAll();
    Point updatePoint(Point point);
    void addPoint(Point point);
    void deletePointById(int pointId);
}
