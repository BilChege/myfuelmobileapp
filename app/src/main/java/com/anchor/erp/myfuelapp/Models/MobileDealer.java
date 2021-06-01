package com.anchor.erp.myfuelapp.Models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class MobileDealer {

    private int id;
    private String name;
    private String stationid;
    private double latitude;
    private double userrating;
    private double longitude;

    public MobileDealer(int id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public MobileDealer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationId() {
        return stationid;
    }

    public void setStationId(String stationId) {
        this.stationid = stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getUserrating() {
        return userrating;
    }

    public void setUserrating(double userrating) {
        this.userrating = userrating;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return  stationid + " ("+name+")";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MobileDealer that = (MobileDealer) o;
        return id == that.id &&
                Double.compare(that.latitude, latitude) == 0 &&
                Double.compare(that.userrating, userrating) == 0 &&
                Double.compare(that.longitude, longitude) == 0 &&
                Objects.equals(name, that.name) &&
                Objects.equals(stationid, that.stationid);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, name, stationid, latitude, userrating, longitude);
    }
}
