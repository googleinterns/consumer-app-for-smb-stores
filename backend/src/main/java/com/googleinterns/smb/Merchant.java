package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.cloud.firestore.annotation.Exclude;

public class Merchant {
    String merchantId;
    String merchantName;
    String merchantAddress;
    double offersAvailed;
    double totalPrice;
    String deliveryTime;
    List<MerchantItem> itemDetails = new ArrayList<>();
    String geoAddress;

    public Merchant() {

    }

    public Merchant(String merchantID) {
        this.merchantId = merchantID;
    }

    public Merchant(String servicingMerchantName, String servicingMerchantAddress, double offers, double totalPrice2,
            String deliveryTime2, String geoAddress2, List<MerchantItem> merchantItemDetails) {
        this.merchantName = servicingMerchantName;
        this.merchantAddress = servicingMerchantAddress;
        this.offersAvailed = offers;
        this.totalPrice = totalPrice2;
        this.deliveryTime = deliveryTime2;
        this.geoAddress = geoAddress2;

        for (MerchantItem item : merchantItemDetails) {
            itemDetails.add(item);
        }

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("merchantName", merchantName);
        result.put("merchantAddress", merchantAddress);
        result.put("offers", offersAvailed);
        result.put("totalPrice", totalPrice);
        result.put("deliveryTime", deliveryTime);
        result.put("geoAddress", geoAddress);
        result.put("items", itemDetails);

        return result;
    }

}