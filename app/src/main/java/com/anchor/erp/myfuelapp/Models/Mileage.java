package com.anchor.erp.myfuelapp.Models;

import java.util.Date;

public class Mileage {

    private int id;
    private Vehicle vehicle;
    private Date dateReported;
    private Double distanceCovered;

    public Mileage() {
    }

    public Mileage(int id, Vehicle vehicle, Date datereported, Double numberOfKilometres) {
        this.id = id;
        this.vehicle = vehicle;
        this.dateReported = datereported;
        this.distanceCovered = numberOfKilometres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Date getDatereported() {
        return dateReported;
    }

    public void setDatereported(Date datereported) {
        this.dateReported = datereported;
    }

    public Double getNumberOfKilometres() {
        return distanceCovered;
    }

    public void setNumberOfKilometres(Double numberOfKilometres) {
        this.distanceCovered = numberOfKilometres;
    }

    @Override
    public String toString() {
        return "Mileage{" +
                "id=" + id +
                ", vehicle=" + vehicle +
                ", datereported=" + dateReported +
                ", numberOfKilometres=" + distanceCovered +
                '}';
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
