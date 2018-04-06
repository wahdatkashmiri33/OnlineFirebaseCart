package com.example.wahdatkashmiri.unknown.Model;

/**
 * Created by WAHDAT KASHMIRI on 05-04-2018.
 */

public class Order {
    private String ProductId;
    private String productName;
    private String Quantity;
    private String Price;
    private String Discount;

    public Order(){

    }

    public Order(String productId, String productName, String quantity, String price, String discount) {
        ProductId = productId;
        this.productName = productName;
        Quantity = quantity;
        Price = price;
        Discount = discount;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
