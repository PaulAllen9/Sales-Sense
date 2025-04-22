package com.example.salessense;

public class Product {
    private String name;
    private int id;
    private int picture;
    private String description;
    private double price;
    private int totalSold;
    private int backStock;

    public Product(String name, int id, int picture, String description, double price){
        this.name=name;
        this.id=id;
        this.picture=picture;
        this.description=description;
        this.price=price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(int totalSold) {
        this.totalSold = totalSold;
    }

    public int getBackStock() {
        return backStock;
    }

    public void setBackStock(int backStock) {
        this.backStock = backStock;
    }
}
