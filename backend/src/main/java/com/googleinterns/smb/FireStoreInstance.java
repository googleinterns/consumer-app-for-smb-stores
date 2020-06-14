package com.googleinterns.smb;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;

@Component
public class FireStoreInstance {

    private Firestore db;
    private static final String ORDER_PATH = "order";
    private static final String CART_ITEMS_PATH = "cart_items";

    public FireStoreInstance() {
        this.db = FirestoreOptions.getDefaultInstance().getService();
    }

    public List<CartItem> deleteItem(String userId, String itemName) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userId).collection(CART_ITEMS_PATH)
                .whereEqualTo("item_name", itemName).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        List<CartItem> deletedItems = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String itemDocID = document.getId();
            DocumentReference docRef = db.collection("cart").document(userId).collection(CART_ITEMS_PATH)
                    .document(itemDocID);
            docRef.delete();
            deletedItems.add(docRef.get().get().toObject(CartItem.class));
        }

        return deletedItems;
    }

    public List<CartItem> updateQuantity(String userID, String itemName, int quantity)
            throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userID).collection(CART_ITEMS_PATH)
                .whereEqualTo("item_name", itemName).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        List<CartItem> updatedItems = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {

            String itemDocID = document.getId();
            DocumentReference docRef = db.collection("cart").document(userID).collection(CART_ITEMS_PATH)
                    .document(itemDocID);

            docRef.update("quantity", quantity);

            updatedItems.add(docRef.get().get().toObject(CartItem.class));

        }

        return updatedItems;
    }

    public List<CartItem> retrieveCartItems(String userId) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userId).collection(CART_ITEMS_PATH).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        List<CartItem> allItems = new ArrayList<>();

        for (QueryDocumentSnapshot doc : documents) {
            CartItem item = doc.toObject(CartItem.class);
            allItems.add(item);
        }

        return allItems;
    }

    public List<OrderDocuments> retrieveOrderDetails(String userId) throws InterruptedException, ExecutionException {

        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH).whereEqualTo("user_id", userId).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        List<OrderDocuments> orderDocs = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String docId = document.getId();
            Order order = document.toObject(Order.class);
            OrderDocuments completeOrder = new OrderDocuments(order);
            ApiFuture<QuerySnapshot> itemFuture = db.collection(ORDER_PATH).document(docId).collection("items").get();
            List<QueryDocumentSnapshot> itemDocs = itemFuture.get().getDocuments();

            for (QueryDocumentSnapshot doc : itemDocs) {
                Item item = doc.toObject(Item.class);
                completeOrder.addItem(item);
            }

            orderDocs.add(completeOrder);
        }

        return orderDocs;

    }

    public CartItem addItemToCart(String userID, CartItem item) throws InterruptedException, ExecutionException {
        DocumentReference docRef = db.collection("cart").document(userID);

        db.collection("cart").document(userID).collection(CART_ITEMS_PATH).get();
        ApiFuture<DocumentReference> addedDoc = db.collection("cart").document(userID).collection(CART_ITEMS_PATH)
                .add(item);

        String addedDocId = addedDoc.get().getId();

        return docRef.collection(CART_ITEMS_PATH).document(addedDocId).get().get().toObject(CartItem.class);

    }

    public void addOrderDoc(OrderDocuments orderDoc) throws InterruptedException, ExecutionException {
        ApiFuture<DocumentReference> addedDocRef = db.collection(ORDER_PATH).add(orderDoc.getOrder());

        String docID = addedDocRef.get().getId();
        List<Item> allItems = orderDoc.getItemDetails();

        for (Item item : allItems) {
            db.collection(ORDER_PATH).document(docID).collection("items").add(item);
        }
    }
}