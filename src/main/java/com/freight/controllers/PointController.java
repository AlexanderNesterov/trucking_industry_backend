package com.freight.controllers;

import com.freight.models.Point;
import com.freight.services.PointService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
public class PointController {

    private PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public List<Point> findAll() {
        return pointService.findAll();
    }

    @GetMapping("/{pointId}")
    public Point findById(@PathVariable int pointId) {
        return pointService.findById(pointId);
    }

    @PutMapping
    public Point updatePoint(@RequestBody Point point) {
        return pointService.updatePoint(point);
    }

    @PostMapping
    public void addPoint(@RequestBody Point point) {
        pointService.addPoint(point);
    }

    @DeleteMapping("/{pointId}")
    public void deletePoint(@PathVariable int pointId) {
        pointService.deletePointById(pointId);
    }
}
