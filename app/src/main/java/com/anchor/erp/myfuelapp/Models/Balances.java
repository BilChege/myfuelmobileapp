package com.anchor.erp.myfuelapp.Models;

public class Balances {

    private double bundle;
    private double account;
    private int points;

    public Balances(double bundle, double account) {
        this.bundle = bundle;
        this.account = account;
    }

    public Balances() {
    }

    public double getBundle() {
        return bundle;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBundle(double bundle) {
        this.bundle = bundle;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }
}
