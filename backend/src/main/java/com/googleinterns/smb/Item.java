package com.googleinterns.smb;

public class Item {
    
    public String item_name;
    public int quantity;
    public float unit_price;

    public Item(){

    }

    public Item(String itemName, int quantity, float price){
        this.item_name = itemName;
        this.quantity = quantity;
        this.unit_price = price;
    }

    @Override
    public String toString(){
        return this.item_name + " " + this.quantity + " " + this.unit_price;
    }
}