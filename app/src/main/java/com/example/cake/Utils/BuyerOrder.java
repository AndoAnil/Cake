package com.example.cake.Utils;

public class BuyerOrder {
    private String storeId;
    private String cakename;
    private String cakeprice;
    private String quantity;
    private String image;
    private String weight;

    public BuyerOrder(){}

    public BuyerOrder(String storeId, String cakename, String cakeprice, String quantity, String image, String weight) {
        this.storeId = storeId;
        this.cakename = cakename;
        this.cakeprice = cakeprice;
        this.quantity = quantity;
        this.image = image;
        this.weight = weight;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCakename() {
        return cakename;
    }

    public void setCakename(String cakename) {
        this.cakename = cakename;
    }

    public String getCakeprice() {
        return cakeprice;
    }

    public void setCakeprice(String cakeprice) {
        this.cakeprice = cakeprice;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "BuyerOrder{" +
                "storeId='" + storeId + '\'' +
                ", cakename='" + cakename + '\'' +
                ", cakeprice='" + cakeprice + '\'' +
                ", quantity='" + quantity + '\'' +
                ", image='" + image + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
