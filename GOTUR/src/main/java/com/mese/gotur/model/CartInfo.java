package com.mese.gotur.model;

import java.util.ArrayList;
import java.util.List;

public class CartInfo {

    private final List<CartLineInfo> cartLines = new ArrayList<>();

    public CartInfo() {

    }

    public List<CartLineInfo> getCartLines() {
        return this.cartLines;
    }

    private CartLineInfo findLineByCode(String code) {
        for (CartLineInfo line : this.cartLines) {
            if (line.getProductInfo().getCode().equals(code)) {
                return line;
            }
        }
        return null;
    }

    public void addProduct(ProductInfo productInfo, int quantity) {
        CartLineInfo line = this.findLineByCode(productInfo.getCode());
        if (line == null) {
            line = new CartLineInfo();
            line.setProductInfo(productInfo);
            this.cartLines.add(line);
        }
        line.setQuantity(line.getQuantity() + quantity);
    }

    public void updateProduct(String code, int quantity) {
        CartLineInfo line = this.findLineByCode(code);
        if (line != null) {
            if (quantity <= 0) {
                this.cartLines.remove(line);
            } else {
                line.setQuantity(quantity);
            }
        }
    }

    public void updateQuantity(String code, String operation) {

        CartLineInfo line = this.findLineByCode(code);
        if (line == null) return;

        int quantity = line.getQuantity();
        if ("increase".equals(operation)) {
            quantity++;
        } else if ("decrease".equals(operation)) {
            quantity--;
        }
        quantity = Math.max(quantity, 0);
        this.updateProduct(line.getProductInfo().getCode(), quantity);

    }

    public void removeProduct(ProductInfo productInfo) {
        CartLineInfo line = this.findLineByCode(productInfo.getCode());
        if (line != null) {
            this.cartLines.remove(line);
        }
    }

    public boolean isEmpty() {
        return this.cartLines.isEmpty();
    }

    public int getQuantityTotal() {
        int quantity = 0;
        for (CartLineInfo line : this.cartLines) {
            quantity += line.getQuantity();
        }
        return quantity;
    }

    public double getAmountTotal() {
        double total = 0;
        for (CartLineInfo line : this.cartLines) {
            total += line.getAmount();
        }
        return total;
    }

    public void validate() {

    }

}