package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ItemDetails {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("item_name")
    private String itemName;

    private int quantity = 0;

    public String getUserId() {
        return userId;
    }

    public void setUserID(String userId) {
        this.userId = userId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}