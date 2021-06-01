package com.anchor.erp.myfuelapp.Models;

import android.graphics.Bitmap;

public class Contact {

    private String name;
    private String phone;
    private Bitmap image;

    public Contact(String name, String phone, Bitmap image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
