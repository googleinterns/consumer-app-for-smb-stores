package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.firestore.annotation.PropertyName;

public class CartItem {

    @JsonProperty("item_name")
    private String itemName;
    private int quantity;

    public CartItem() {

    }

    public CartItem(String itemName, int quantity) {
        this.itemName = itemName;
        this.quantity = quantity;
    }

    @PropertyName("item_name")
    public String getItemName() {
        return itemName;
    }

    @PropertyName("item_name")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }
}