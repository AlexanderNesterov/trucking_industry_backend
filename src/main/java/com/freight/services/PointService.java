package com.freight.services;

import com.freight.models.Point;

import java.util.List;

public interface PointService {

    Point findById(int pointId);
    List<Point> findAll();
    Point updatePoint(Point point);
    void addPoint(Point point);
    void deletePointById(int pointId);
}
