package com.example.cake.Utils;

public class BuyerModel {
    private String name;
    private String address;
    private String email;
    private String mno;
    private String imageUrl;

    public BuyerModel(String name, String address, String email, String mno, String imageUrl) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.mno = mno;
        this.imageUrl = imageUrl;
    }

    public BuyerModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMno() {
        return mno;
    }

    public void setMno(String mno) {
        this.mno = mno;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "BuyerModel{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", mno='" + mno + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
