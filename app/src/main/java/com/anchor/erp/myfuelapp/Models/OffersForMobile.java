package com.anchor.erp.myfuelapp.Models;

import java.util.Date;

public class OffersForMobile {

    private int id;
    private String promoname;
    private String promocode;
    private String promodesc;
    private int points;
    private Date expiry;

    public OffersForMobile(int id, String promoname, String promodesc, int points, Date expiry) {
        this.id = id;
        this.promoname = promoname;
        this.promodesc = promodesc;
        this.points = points;
        this.expiry = expiry;
    }

    public OffersForMobile() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPromoname() {
        return promoname;
    }

    public void setPromoname(String promoname) {
        this.promoname = promoname;
    }

    public String getPromodesc() {
        return promodesc;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public void setPromodesc(String promodesc) {
        this.promodesc = promodesc;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }
}
