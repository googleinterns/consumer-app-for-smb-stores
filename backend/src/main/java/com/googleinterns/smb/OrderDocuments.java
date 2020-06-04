package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.List;

public class OrderDocuments {

    private Order order;
    private List<Item> itemDetails = new ArrayList<>();

    public OrderDocuments(Order order){
        this.order = order;
    }

    public List<Item> getAllItems(){
        return itemDetails;
    }

    public void addItem(Item item){
        this.itemDetails.add(item);
    }

    public Order getOrder(){
        return order;
    }
    
}