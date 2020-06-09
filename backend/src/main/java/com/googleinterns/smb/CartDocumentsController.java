package com.googleinterns.smb;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class CartDocumentsController {

    @Autowired
    private FireStoreInstance fireStoreInstance;

    @GetMapping(value = "/getItemsInCart")
    public List<CartItem> getItemsInCart(@RequestParam String user) throws InterruptedException, ExecutionException {
        return fireStoreInstance.retrieveCartItems(user);
    }

    @PostMapping(value = "/updateItemQuantity")
    public List<CartItem> updateItemQuantity(@RequestBody ItemDetails itemDetails) throws InterruptedException, ExecutionException {
        return fireStoreInstance.updateQuantity(itemDetails.getUserId(), itemDetails.getItemName(), itemDetails.getQuantity());
    }

    @PostMapping(value = "/removeItemFromCart")
    public List<CartItem> removeItem(@RequestBody ItemDetails itemDetails) throws InterruptedException, ExecutionException {
        return fireStoreInstance.deleteItem(itemDetails.getUserId(), itemDetails.getItemName());
    }


    @PostMapping(value = "/addNewItem")
    public CartItem addNewItem(@RequestBody ItemDetails itemRequest) throws InterruptedException, ExecutionException {
        String userId = itemRequest.getUserId();
        CartItem item = new CartItem(itemRequest.getItemName(), itemRequest.getQuantity());

        return fireStoreInstance.addItemToCart(userId, item);
    }
}