package com.anchor.erp.myfuelapp.Models;

import java.util.Collection;
import java.util.List;

public class MobileUser {

    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String pin;
    private String email;
    private String userfeedback;
    private boolean ratedapp;
    private double rating;
    private String role;
    private String accountPassword;
    private Balances balances;
    private List<Vehicle> vehicles;
    private Collection<Purchase> purchases;
    private Collection<FuelCar> usages;

    public MobileUser() {
    }

    public MobileUser(int id, String firstName, String lastName, String phone, String email, String accountPassword, List<Vehicle> vehicles, Collection<Purchase> purchases) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.accountPassword = accountPassword;
        this.vehicles = vehicles;
        this.purchases = purchases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPin() {
        return pin;
    }

    public Balances getBalances() {
        return balances;
    }

    public void setBalances(Balances balances) {
        this.balances = balances;
    }

    public Collection<FuelCar> getUsages() {
        return usages;
    }

    public void setUsages(Collection<FuelCar> usages) {
        this.usages = usages;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserfeedback() {
        return userfeedback;
    }

    public void setUserfeedback(String userfeedback) {
        this.userfeedback = userfeedback;
    }

    public boolean isRatedapp() {
        return ratedapp;
    }

    public void setRatedapp(boolean ratedapp) {
        this.ratedapp = ratedapp;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Collection<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(Collection<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String toString() {
        return "MobileUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\n' +
                ", lastName='" + lastName + '\n' +
                ", phone='" + phone + '\n' +
                ", pin='" + pin + '\n' +
                ", email='" + email + '\n' +
                ", userfeedback='" + userfeedback + '\n' +
                ", ratedapp=" + ratedapp + '\n'+
                ", rating=" + rating + '\n'+
                ", role='" + role + '\n' +
                ", accountPassword='" + accountPassword + '\n' +
                ", balances=" + balances + '\n'+
                ", vehicles=" + vehicles + '\n'+
                ", purchases=" + purchases + '\n'+
                ", usages=" + usages + '\n'+
                '}';
    }
}
