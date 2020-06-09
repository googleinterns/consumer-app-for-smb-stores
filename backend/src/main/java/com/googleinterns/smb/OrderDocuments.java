package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class OrderDocuments {

    private Order order;
    @JsonProperty("all_items")
    private List<Item> itemDetails = new ArrayList<>();

    public OrderDocuments(Order order) {
        this.order = order;
    }

    public List<Item> getItemDetails() {
        return itemDetails;
    }

    public void addItem(Item item) {
        this.itemDetails.add(item);
    }

    public Order getOrder() {
        return order;
    }

}