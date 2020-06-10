package com.googleinterns.smb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrderGeneration {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("servicing_merchant_name")
    private String servicingMerchantName;

    @JsonProperty("servicing_merchant_address")
    private String servicingMerchantAddress;

    private String offers;

    @JsonProperty("item_details")
    private List<Item> itemDetails;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServicingMerchantName() {
        return this.servicingMerchantName;
    }

    public void setServicingMerchantName(String merchantName) {
        this.servicingMerchantName = merchantName;
    }

    public String getServicingMerchantAddress() {
        return this.servicingMerchantAddress;
    }

    public void setServicingMerchantAddress(String merchantAddress) {
        this.servicingMerchantAddress = merchantAddress;
    }

    public String getOffers() {
        return this.offers;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }

    public List<Item> getItemDetails() {
        return this.itemDetails;
    }

    public void setItemDetails(List<Item> itemDetails) {
        this.itemDetails = itemDetails;
    }
}