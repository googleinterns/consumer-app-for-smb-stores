package com.googleinterns.smb;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
public class OrderStatusUpdatesController {

    @Autowired
    private FireStoreInstance fireStoreInstance;

    private static Map<String, Object> statusUpdate = new HashMap<>();

    @PostMapping("/updateOrderStatus")
    public void statusUpdate(@RequestBody OrderStatusUpdates updates) throws ExecutionException, InterruptedException {
        DatabaseReference orderStatusCursor = FirebaseDatabase.getInstance().getReference();

        DatabaseReference orderStatus = orderStatusCursor.child("newNotifications").child(updates.getUserId()).child(updates.getOrderId()).child(updates.getStatus());
        statusUpdate.put("trigger", 1);
        orderStatus.updateChildrenAsync(statusUpdate);
        if (updates.getStatus().equalsIgnoreCase("packageDelivered")) {
            fireStoreInstance.updateOrderStatus(updates.getOrderId());
        }
    }
}
