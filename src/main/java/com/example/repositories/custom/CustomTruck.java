package com.example.repositories.custom;

import java.util.List;

public interface CustomTruck<T> {
    List<Object> getRegNumAndShiftSize(int truckId);
}