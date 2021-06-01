package com.anchor.erp.myfuelapp.Models;

import java.util.Date;

public class MobileRedemptions {

    private int id;
    private MobileUser user;
    private String stationId;
    private OffersForMobile offer;
    private Date datepurchased;

    public MobileRedemptions(int id, MobileUser mobileUser, OffersForMobile offers, Date dateredeemed) {
        this.id = id;
        this.user = mobileUser;
        this.offer = offers;
        this.datepurchased = dateredeemed;
    }

    public MobileRedemptions() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MobileUser getMobileUser() {
        return user;
    }

    public void setMobileUser(MobileUser mobileUser) {
        this.user = mobileUser;
    }

    public OffersForMobile getOffers() {
        return offer;
    }

    public void setOffers(OffersForMobile offers) {
        this.offer = offers;
    }

    public Date getDateredeemed() {
        return datepurchased;
    }

    public void setDateredeemed(Date dateredeemed) {
        this.datepurchased = dateredeemed;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
