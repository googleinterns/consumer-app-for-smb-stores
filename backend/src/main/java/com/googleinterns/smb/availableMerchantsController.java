package com.googleinterns.smb;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.springframework.web.bind.annotation.*;

@RestController
public class availableMerchantsController {

    @RequestMapping(value = "/sendBid", method = RequestMethod.POST)
    public void sendBid(@RequestBody Merchant merchantRequest) throws Exception {

        Map<String, Object> merchantdetails = merchantRequest.toMap();

        Map<String, Object> merchantChildUpdates = new HashMap<>();
        merchantChildUpdates.put(merchantRequest.getMerchantId(), merchantdetails);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        DatabaseReference merchantbase = mDatabase.child("users").child(merchantRequest.getUserId())
                .child(merchantRequest.getOrderId()).child("merchants");

        merchantbase.updateChildrenAsync(merchantChildUpdates);
    }

}