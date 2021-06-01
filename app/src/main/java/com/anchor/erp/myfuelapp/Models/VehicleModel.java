package com.anchor.erp.myfuelapp.Models;

public class VehicleModel {

    private int id;
    private String model;

    public VehicleModel(int id, String model) {
        this.id = id;
        this.model = model;
    }

    public VehicleModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
