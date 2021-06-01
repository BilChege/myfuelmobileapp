package com.anchor.erp.myfuelapp.Models;

import java.util.Collection;

public class FuelPackage {

    private int id;
    private double amount;
    private int expirydays;
    private double priceOfPackage;
    private int points;
    private String typeOfPackage;
    private Collection<Purchase> purchases;

    public FuelPackage() {
    }

    public FuelPackage(int id, double amountOffuel, double priceOfPackage, String typeOfPackage, Collection<Purchase> purchases) {
        this.id = id;
        this.amount = amountOffuel;
        this.priceOfPackage = priceOfPackage;
        this.typeOfPackage = typeOfPackage;
        this.purchases = purchases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getExpirydays() {
        return expirydays;
    }

    public void setExpirydays(int expirydays) {
        this.expirydays = expirydays;
    }

    public double getAmountOffuel() {
        return amount;
    }

    public void setAmountOffuel(double amountOffuel) {
        this.amount = amountOffuel;
    }

    public double getPriceOfPackage() {
        return priceOfPackage;
    }

    public void setPriceOfPackage(double priceOfPackage) {
        this.priceOfPackage = priceOfPackage;
    }

    public String getTypeOfPackage() {
        return typeOfPackage;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setTypeOfPackage(String typeOfPackage) {
        this.typeOfPackage = typeOfPackage;
    }

    public Collection<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Collection<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "FuelPackage{" +
                "id=" + id +
                ", amountOffuel=" + amount +
                ", priceOfPackage=" + priceOfPackage +
                ", typeOfPackage='" + typeOfPackage + '\'' +
                ", purchases=" + purchases +
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
