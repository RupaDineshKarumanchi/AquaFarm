package com.example.aquafarm;

public class FeedStockModel {

    String brand;
    String feedNo;
    String avlQty;

    public FeedStockModel(String brand, String feedNo, String avlQty) {
        this.brand = brand;
        this.feedNo = feedNo;
        this.avlQty = avlQty;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getFeedNo() {
        return feedNo;
    }

    public void setFeedNo(String feedNo) {
        this.feedNo = feedNo;
    }

    public String getAvlQty() {
        return avlQty;
    }

    public void setAvlQty(String avlQty) {
        this.avlQty = avlQty;
    }
}
