package com.googleinterns.smb;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.google.firebase.database.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")

@RestController
public class MerchantsController {

    final Semaphore semaphore = new Semaphore(0);
    @RequestMapping(value = "/sendBid", method = RequestMethod.POST)
    public void sendBid(@RequestBody Merchant merchantRequest) throws Exception {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> merchantdetails = merchantRequest.toMap();

        Map<String, Object> merchantChildUpdates = new HashMap<>();
        merchantChildUpdates.put(merchantRequest.getMerchantId(), merchantdetails);

        DatabaseReference merchantbase = mDatabase.child("users").child(merchantRequest.getUserId()).child(merchantRequest.getOrderId()).child("merchants");
        merchantbase.updateChildrenAsync(merchantChildUpdates);
                    
    }

}