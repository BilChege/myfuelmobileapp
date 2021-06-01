package com.anchor.erp.myfuelapp.Models;

import java.util.Date;

public class Purchase {

    private int id;
    private MobileUser user;
    private FuelPackage aPackage;
    private Date datePurchased;
    private Date expiryDate;
    private Balances balances;

    public Purchase() {
    }

    public Purchase(int id, MobileUser user, FuelPackage aPackage, Date datePurchased, Date expiryDate) {
        this.id = id;
        this.user = user;
        this.aPackage = aPackage;
        this.datePurchased = datePurchased;
        this.expiryDate = expiryDate;
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

    public FuelPackage getaPackage() {
        return aPackage;
    }

    public void setaPackage(FuelPackage aPackage) {
        this.aPackage = aPackage;
    }

    public Date getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(Date datePurchased) {
        this.datePurchased = datePurchased;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
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

    @Override
    public String toString() {
        return super.toString();
    }
}
