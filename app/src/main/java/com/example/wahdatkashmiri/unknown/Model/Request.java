package com.example.wahdatkashmiri.unknown.Model;

import java.util.List;

/**
 * Created by WAHDAT KASHMIRI on 05-04-2018.
 */

public class Request {
    private String email;
    private String name;
    private String address;
    private String total;
    private String status;
    private String contact;
    private String food_name;
    private String quantity;
    private List<Order> items;


    public Request(String email,String name, String address,String contact, String food_name,String total,String quantity ) {
        this.email = email;
        this.name=name;
        this.address = address;
        this.contact=contact;
        this.food_name=food_name;
        this.total = total;
        this.quantity=quantity;
        this.items = items;
        this.status= "0";
    }


    public Request() {
    }

    public Request(String email, String address, String contact, String total, List<Order> items) {
        this.email = email;

        this.address = address;
        this.contact=contact;
        this.total = total;
        this.items = items;
        this.status= "0";
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getContact() {
        return contact;
    }


    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }
}
