package com.example.realworld.domain.user.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class RiderLocation {
    private double longitude;

    private double latitude;

    public void updateLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void updateLatitude(double latitude) {
        this.latitude = latitude;
    }

}


