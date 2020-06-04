package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.*;

class User{
    public String userID;
}

class OrderGeneration{
    public String userId;
    public String servicingMerchantName;
    public String servicingMerchantAddress;
    public String offers;
    public List<Item> itemDetails;
}

@RestController
public class OrderDocumentsController {
    
    @RequestMapping(value = "/placeOrder", method = RequestMethod.POST)
	public void placeOrder(@RequestBody OrderGeneration orderRequest) throws Exception {
        Order order = new Order(orderRequest.userId, orderRequest.servicingMerchantName, 
              orderRequest.servicingMerchantAddress, orderRequest.offers);

        OrderDocuments orderDoc = new OrderDocuments(order);

        for (Item item: orderRequest.itemDetails){
            orderDoc.addItem(item);
            SMBKiranaApplication.fireStoreInstance.deleteItem(orderRequest.userId, item.item_name);
        }

		SMBKiranaApplication.fireStoreInstance.addOrderDoc(orderDoc);
	}

	@RequestMapping(value="/getOrderDetails")
	public ArrayList<OrderDocuments> getOrderDetails(@RequestParam String user) throws Exception{
		ArrayList<OrderDocuments> allOrders = SMBKiranaApplication.fireStoreInstance.retrieveOrderDetails(user);
		return allOrders;
	}
}