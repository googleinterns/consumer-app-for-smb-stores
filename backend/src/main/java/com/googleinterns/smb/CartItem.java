package com.googleinterns.smb;

public class CartItem{
    private String item_name;
    private int quantity;

    public CartItem(){

    }

    public CartItem(String itemName, int quantity){
        this.item_name = itemName;
        this.quantity = quantity;
    }


    public String getItem_name(){
        return item_name;
    }

    public int getQuantity(){
        return quantity;
    }
}