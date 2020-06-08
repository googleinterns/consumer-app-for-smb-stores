package com.googleinterns.smb;

public class MerchantItem {

    public String itemName;
    public int quantity;
    public float unitPrice;
    public boolean isAvailable;

    public MerchantItem() {

    }

    public MerchantItem(String MerchantItemName, int quantity, float price, boolean isAvailable) {
        this.itemName = MerchantItemName;
        this.quantity = quantity;
        this.unitPrice = price;
        this.isAvailable = isAvailable;
    }
}