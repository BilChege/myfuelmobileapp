package com.anchor.erp.myfuelapp.Models;

import java.util.Collection;

public class Vehicle {

    private int id;
    private String regno;
    private String make;
    private int makeid;
    private boolean active;
    private int modelid;
    private MobileUser owner;
    private String CCs;
    private String enginetype;
    private Collection<FuelCar> fuelingInstances;
    private Collection<Mileage> mileagereports;

    public Vehicle() {
    }

    public Vehicle(int id, String regno, MobileUser owner, Collection<FuelCar> fuelingInstances,Collection<Mileage> mileagereports) {
        this.id = id;
        this.regno = regno;
        this.owner = owner;
        this.fuelingInstances = fuelingInstances;
        this.mileagereports = mileagereports;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEnginetype() {
        return enginetype;
    }

    public void setEnginetype(String enginetype) {
        this.enginetype = enginetype;
    }

    public String getCCs() {
        return CCs;
    }

    public void setCCs(String CCs) {
        this.CCs = CCs;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getMakeid() {
        return makeid;
    }

    public void setMakeid(int makeid) {
        this.makeid = makeid;
    }

    public int getModelid() {
        return modelid;
    }

    public void setModelid(int modelid) {
        this.modelid = modelid;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public MobileUser getOwner() {
        return owner;
    }

    public void setOwner(MobileUser owner) {
        this.owner = owner;
    }

    public Collection<FuelCar> getFuelingInstances() {
        return fuelingInstances;
    }

    public Collection<Mileage> getMileagereports() {
        return mileagereports;
    }

    public void setMileagereports(Collection<Mileage> mileagereports) {
        this.mileagereports = mileagereports;
    }

    public void setFuelingInstances(Collection<FuelCar> fuelingInstances) {
        this.fuelingInstances = fuelingInstances;
    }

    @Override
    public String toString() {
        return regno+" ("+make+")";
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
