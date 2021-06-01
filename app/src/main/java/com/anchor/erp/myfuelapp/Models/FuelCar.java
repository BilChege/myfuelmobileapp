package com.anchor.erp.myfuelapp.Models;

import java.util.Date;

public class FuelCar {

    private int id;
    private double amount;
    private Vehicle vehicle;
    private Date dateFueled;
    private String stationid;
    private MobileUser user;
    private String feedBack;
    private Balances balances;

    public FuelCar() {
    }

    public FuelCar(int id, MobileUser user,String stationid, double amountOfFuel, Vehicle vehicle, Date dateFueled) {
        this.id = id;
        this.amount = amountOfFuel;
        this.vehicle = vehicle;
        this.dateFueled = dateFueled;
        this.stationid = stationid;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Balances getBalances() {
        return balances;
    }

    public void setBalances(Balances balances) {
        this.balances = balances;
    }

    public MobileUser getUser() {
        return user;
    }

    public void setUser(MobileUser user) {
        this.user = user;
    }

    public double getAmountOfFuel() {
        return amount;
    }

    public void setAmountOfFuel(double amountOfFuel) {
        this.amount = amountOfFuel;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Date getDateFueled() {
        return dateFueled;
    }

    public void setDateFueled(Date dateFueled) {
        this.dateFueled = dateFueled;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    @Override
    public String toString() {
        return "FuelCar{" +
                "id=" + id +
                ", amountOfFuel=" + amount +
                ", vehicle=" + vehicle +
                ", dateFueled=" + dateFueled +
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
