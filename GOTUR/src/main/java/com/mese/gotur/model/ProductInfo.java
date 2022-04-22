package com.mese.gotur.model;

import com.mese.gotur.entity.Product;

public class ProductInfo {

    private String code;

    private String name;

    private double unitPrice;

    private int quantityInStock;

    public ProductInfo() {
    }

    public ProductInfo(Product product) {
        this.code = product.getCode();
        this.name = product.getName();
        this.unitPrice = product.getUnitPrice();
        this.quantityInStock = product.getQuantityInStock();
    }

    // Using in JPA/Hibernate query
    public ProductInfo(String code, String name, double unitPrice, int quantityInStock) {
        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantityInStock = quantityInStock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
