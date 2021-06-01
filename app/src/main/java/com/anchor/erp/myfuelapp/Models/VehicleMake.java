package com.anchor.erp.myfuelapp.Models;

import java.util.List;

public class VehicleMake {

    private int id;
    private String vehiclemake;
    private List<VehicleModel> models;

    public VehicleMake(int id, String vehicleMake) {
        this.id = id;
        this.vehiclemake = vehicleMake;
    }

    public VehicleMake() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleMake() {
        return vehiclemake;
    }

    public void setVehicleMake(String vehicleMake) {
        this.vehiclemake = vehicleMake;
    }

    public List<VehicleModel> getModels() {
        return models;
    }

    public void setModels(List<VehicleModel> models) {
        this.models = models;
    }
}
