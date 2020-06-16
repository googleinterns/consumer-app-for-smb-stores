package com.googleinterns.smb;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class OrderDocumentsController {

    @Autowired
    private FireStoreInstance fireStoreInstance;

    @PostMapping(value = "/placeOrder")
    public void placeOrder(@RequestBody OrderGeneration orderRequest) throws ExecutionException, InterruptedException {
        Order order = new Order(orderRequest.getUserId(), orderRequest.getOrderId(), orderRequest.getServicingMerchantName(), orderRequest.getServicingMerchantAddress(), orderRequest.getOffers());
        OrderDocuments orderDoc = new OrderDocuments(order);

        for (Item item : orderRequest.getItemDetails()) {
            orderDoc.addItem(item);
        }
        fireStoreInstance.addOrderDoc(orderDoc);
    }

    @GetMapping(value = "/getOrderDetails")
    public List<OrderDocuments> getOrderDetails(@RequestParam String user) throws ExecutionException, InterruptedException {
        return fireStoreInstance.retrieveOrderDetails(user);
    }

    @GetMapping(value = "/getOngoingOrders")
    public List<OrderDocuments> getOngoingOrders(@RequestParam String user) throws ExecutionException, InterruptedException {
        return fireStoreInstance.getOngoingOrders(user);
    }

    @PostMapping(value="/updateOrderDetails")
    public void updateDetails(@RequestBody OrderGeneration orderRequest) throws ExecutionException, InterruptedException {
        Order order = new Order(orderRequest.getUserId(), orderRequest.getOrderId(), orderRequest.getServicingMerchantName(), orderRequest.getServicingMerchantAddress(), orderRequest.getOffers());
        OrderDocuments orderDoc = new OrderDocuments(order);

        for (Item item : orderRequest.getItemDetails()) {
            orderDoc.addItem(item);
        }
        fireStoreInstance.updateOrderDoc(orderDoc);
    }
}