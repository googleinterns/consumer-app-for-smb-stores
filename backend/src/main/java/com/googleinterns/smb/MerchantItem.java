package com.googleinterns.smb;

public class MerchantItem {

    private String merchantItemName;
    private int quantity;
    private float unitPrice;
    private boolean isAvailable;

    public MerchantItem() {

    }

    public void setmerchantItemName(String merchantItemName) {
        this.merchantItemName = merchantItemName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getMerchantItemName() {
        return this.merchantItemName;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public float getUnitPrice() {
        return this.unitPrice;
    }

    public boolean getIsAvailable() {
        return this.isAvailable;
    }

}