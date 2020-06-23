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
        DatabaseReference totalRating = mDatabase.child("MerchantRatings").child(merchantRequest.getMerchantId()).child("TotalRatings");
        DatabaseReference numberOfOrders = mDatabase.child("MerchantRatings").child(merchantRequest.getMerchantId()).child("Number_of_Orders");
        String[] rating=new String[2];
        String[] orders=new String[1];
        totalRating.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int index=0;
                for(DataSnapshot keynode: dataSnapshot.getChildren())
                {
                    rating[index]=keynode.getValue().toString();
                    index=index+1;
                }
                semaphore.release();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        semaphore.acquire();
        numberOfOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot keynode : dataSnapshot.getChildren()) {
                    orders[0] = keynode.getValue().toString();
                }
                semaphore.release();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
        semaphore.acquire();
         float timeAvg=Float.parseFloat(rating[0])/Float.parseFloat(orders[0]);
         float qualityAvg=Float.parseFloat(rating[1])/Float.parseFloat(orders[0]);
         float finalRating=(timeAvg+qualityAvg)/2;
         merchantRequest.setRating(4);
        Map<String, Object> merchantdetails = merchantRequest.toMap();

        Map<String, Object> merchantChildUpdates = new HashMap<>();
        merchantChildUpdates.put(merchantRequest.getMerchantId(), merchantdetails);

        DatabaseReference merchantbase = mDatabase.child("users").child(merchantRequest.getUserId()).child(merchantRequest.getOrderId()).child("merchants");
        merchantbase.updateChildrenAsync(merchantChildUpdates);
                    
    }

}