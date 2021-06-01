package com.anchor.erp.myfuelapp.Models;

public class LocObj {

    private double lat;
    private double lon;

    public LocObj(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public LocObj() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
