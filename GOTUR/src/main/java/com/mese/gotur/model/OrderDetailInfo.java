package com.mese.gotur.model;

public class OrderDetailInfo {

    private String id;

    private String productCode;

    private String productName;

    private int quantity;

    private double unitPrice;

    private double amount;
 
    public OrderDetailInfo() {
 
    }
 
    // Using for JPA/Hibernate Query.
    public OrderDetailInfo(String id, String productCode, String productName, int quantity, double unitPrice, double amount) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public String getProductCode() {
        return productCode;
    }
 
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
 
    public String getProductName() {
        return productName;
    }
 
    public void setProductName(String productName) {
        this.productName = productName;
    }
 
    public int getQuantity() {
        return quantity;
    }
 
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
 
    public double getUnitPrice() {
        return unitPrice;
    }
 
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
 
    public double getAmount() {
        return amount;
    }
 
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
