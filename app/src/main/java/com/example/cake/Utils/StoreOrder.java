package com.example.cake.Utils;

public class StoreOrder {
    private String custumerId;
    private String cakename;
    private String price;
    private String quantity;
    private String image;
    private String weight;

    public StoreOrder(){}

    public StoreOrder(String custumerId, String cakename, String price, String quantity, String image, String weight) {
        this.custumerId = custumerId;
        this.cakename = cakename;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "StoreOrder{" +
                "custumerId='" + custumerId + '\'' +
                ", cakename='" + cakename + '\'' +
                ", price='" + price + '\'' +
                ", quantity='" + quantity + '\'' +
                ", image='" + image + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }

    public String getCustumerId() {
        return custumerId;
    }

    public void setCustumerId(String custumerId) {
        this.custumerId = custumerId;
    }

    public String getCakename() {
        return cakename;
    }

    public void setCakename(String cakename) {
        this.cakename = cakename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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
}
