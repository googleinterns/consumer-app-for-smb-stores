package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;

public class Item {

    @JsonProperty("item_name")
    private String itemName;
    private int quantity;
    @JsonProperty("unit_price")
    private float unitPrice;
    @JsonProperty("EAN")
    private String EAN;

    public Item() {

    }

    public Item(String itemName, int quantity, float price, String EAN) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unitPrice = price;
        this.EAN = EAN;
    }

    @PropertyName("item_name")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @PropertyName("unit_price")
    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    @PropertyName("item_name")
    public String getItemName() {
        return itemName;
    }

    @PropertyName("EAN")
    public void setEAN(String EAN) {
        this.EAN = EAN;
    }

    @PropertyName("EAN")
    public String getEAN() {
        return EAN;
    }

    public int getQuantity() {
        return quantity;
    }

    @PropertyName("unit_price")
    public float getUnitPrice() {
        return unitPrice;
    }

}