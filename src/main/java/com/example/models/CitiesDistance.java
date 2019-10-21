package com.example.models;

import javax.persistence.*;

@Entity
@Table(name = "cities_distances", schema = "freight")
public class CitiesDistance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_city_id")
    private int firstCityId;

    @Column(name = "second_city_id")
    private int secondCityId;

    @Column(name = "distance")
    private double distance;

    public CitiesDistance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFirstCityId() {
        return firstCityId;
    }

    public void setFirstCityId(int firstCityId) {
        this.firstCityId = firstCityId;
    }

    public int getSecondCityId() {
        return secondCityId;
    }

    public void setSecondCityId(int secondCityId) {
        this.secondCityId = secondCityId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
