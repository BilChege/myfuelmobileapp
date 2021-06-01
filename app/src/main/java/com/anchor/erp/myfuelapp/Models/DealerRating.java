package com.anchor.erp.myfuelapp.Models;

public class DealerRating {

    private int id;
    private int dealer;
    private int user;
    private String comments;
    private double rating;

    public DealerRating(int id, int dealer, int user, String comments, int rating) {
        this.id = id;
        this.dealer = dealer;
        this.user = user;
        this.comments = comments;
        this.rating = rating;
    }

    public DealerRating() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDealer() {
        return dealer;
    }

    public void setDealer(int dealer) {
        this.dealer = dealer;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
