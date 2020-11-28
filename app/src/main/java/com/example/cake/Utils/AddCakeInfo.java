package com.example.cake.Utils;

public class AddCakeInfo {
    private String cakename;
    private String quantity;
    private String weight;
    private String imageUrl;
    private String price;
    private String storeId;

    public AddCakeInfo(){}

    public AddCakeInfo(String cakename, String quantity, String weight, String imageUrl, String price, String storeId) {
        this.cakename = cakename;
        this.quantity = quantity;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.price = price;
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "AddCakeInfo{" +
                "cakename='" + cakename + '\'' +
                ", quantity='" + quantity + '\'' +
                ", weight='" + weight + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price='" + price + '\'' +
                ", storeId='" + storeId + '\'' +
                '}';
    }

    public String getCakename() {
        return cakename;
    }

    public void setCakename(String cakename) {
        this.cakename = cakename;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
}
