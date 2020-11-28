package com.example.cake.Utils;

public class MakerMOdel {
    private String restname;
    private String address;
    private String email;
    private String mno;
    private String imageUrl;

    public MakerMOdel(String restname, String address, String email, String mno, String imageUrl) {
        this.restname = restname;
        this.address = address;
        this.email = email;
        this.mno = mno;
        this.imageUrl = imageUrl;
    }
    public MakerMOdel(){}

    @Override
    public String toString() {
        return "MakerMOdel{" +
                "restname='" + restname + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", mno='" + mno + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public String getRestname() {
        return restname;
    }

    public void setRestname(String restname) {
        this.restname = restname;
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
}
