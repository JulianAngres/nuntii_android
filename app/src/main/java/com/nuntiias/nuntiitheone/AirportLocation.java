package com.nuntiias.nuntiitheone;

public class AirportLocation {

    private float lat;
    private float lon;

    public AirportLocation(int lat, int lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }
}
