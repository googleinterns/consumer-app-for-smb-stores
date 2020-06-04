package com.googleinterns.smb;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.*;

class ItemDetails{
	public String userID;
	public String itemName;
	public int quantity = 0;

	@Override
	public String toString(){
		return userID;
	}
}

@RestController
public class CartDocumentsController {

	@RequestMapping(value="/getItemsInCart")
	public ArrayList<CartItem> getItemsInCart(@RequestParam String user)
			throws InterruptedException, ExecutionException {
		ArrayList<CartItem> allItemsInCart = SMBKiranaApplication.fireStoreInstance.retrieveCartItems(user);
		return allItemsInCart;
	}

	@RequestMapping(value="/updateItemQuantity", method=RequestMethod.POST)
	public List<CartItem> updateItemQuantity(@RequestBody ItemDetails itemDetails)
			throws InterruptedException, ExecutionException {
		return SMBKiranaApplication.fireStoreInstance.updateQuantity(itemDetails.userID, itemDetails.itemName, itemDetails.quantity);
	}

	@RequestMapping(value="/removeItemFromCart", method=RequestMethod.POST)
	public List<CartItem> removeItem(@RequestBody ItemDetails itemDetails) throws InterruptedException, ExecutionException {
		return SMBKiranaApplication.fireStoreInstance.deleteItem(itemDetails.userID, itemDetails.itemName);
	}
	

	@RequestMapping(value="/addNewItem", method=RequestMethod.POST)
	public CartItem addNewItem(@RequestBody ItemDetails itemRequest) throws InterruptedException, ExecutionException {
		String userId = itemRequest.userID;
		CartItem item = new CartItem(itemRequest.itemName, itemRequest.quantity);

		return SMBKiranaApplication.fireStoreInstance.addItemToCart(userId, item);
	}
}