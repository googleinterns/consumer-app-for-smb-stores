package com.googleinterns.smb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.web.bind.annotation.*;

class availableMerchant {
    public String merchantId;
    public String servicingMerchantName;
    public String servicingMerchantAddress;
    public double offers;
    public double totalPrice;
    public String deliveryTime;
    public List<MerchantItem> merchantItemDetails;
    public String userId;
    public String orderId;
    public String geoAddress;
}

@RestController
public class availableMerchantsController {

    @RequestMapping(value = "/sendBid", method = RequestMethod.POST)
    public void sendBid(@RequestBody availableMerchant merchantRequest) throws Exception {

        Merchant merchant = new Merchant(merchantRequest.servicingMerchantName,
                merchantRequest.servicingMerchantAddress, merchantRequest.offers, merchantRequest.totalPrice,
                merchantRequest.deliveryTime, merchantRequest.geoAddress, merchantRequest.merchantItemDetails);

        Map<String, Object> merchantdetails = merchant.toMap();

        Map<String, Object> merchantChildUpdates = new HashMap<>();
        merchantChildUpdates.put(merchantRequest.merchantId, merchantdetails);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference merchantbase = mDatabase.child("users").child(merchantRequest.userId)
                .child(merchantRequest.orderId).child("merchants");

        merchantbase.updateChildrenAsync(merchantChildUpdates);
    }

}