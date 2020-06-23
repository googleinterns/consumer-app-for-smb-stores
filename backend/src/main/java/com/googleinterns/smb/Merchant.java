package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.annotation.Exclude;

public class Merchant {
    private String merchantId;
    private String merchantName;
    private String merchantAddress;
    private float offersAvailed;
    private double totalPrice;
    private long deliveryTime;
    private List<MerchantItem> itemDetails = new ArrayList<>();
    private String userId;
    private String orderId;
    private float rating;
    private Map<String, Double> geoLocation = new HashMap<>();

    public Merchant() {

    }

    public void setMerchantId(String MerchantId) {
        this.merchantId = MerchantId;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public void setOffersAvailed(float offersAvailed) {
        this.offersAvailed = offersAvailed;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDeliveryTime(long deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setItemDetails(List<MerchantItem> items) {
        this.itemDetails = items;
    }

    public void setLocation(Map<String, Double> geoLocation) {
        this.geoLocation = geoLocation;
    }

    public void setRating(float rating) {
        this.rating=rating;
    }

    public String getMerchantId() {
        return this.merchantId;
    }

    public String getMerchantName() {
        return this.merchantName;
    }

    public String getMerchantAddress() {
        return this.merchantAddress;
    }

    public float getOffers() {
        return this.offersAvailed;
    }

    public double getTotalPrice() {
        return this.totalPrice;
    }

    public long getDeliveryTime() {
        return this.deliveryTime;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public List<MerchantItem> getItemDetails() {
        return this.itemDetails;
    }

    public Map<String, Double> getGeoLocation() {
        return this.geoLocation;
    }
    public float getRating(){
        return this.rating;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("merchantId", merchantId);
        result.put("merchantName", merchantName);
        result.put("merchantAddress", merchantAddress);
        result.put("offers", offersAvailed);
        result.put("totalPrice", totalPrice);
        result.put("deliveryTime", deliveryTime);
        result.put("itemDetails", itemDetails);
        result.put("geoLocation", geoLocation);
        result.put("rating", rating);
        return result;
    }

}