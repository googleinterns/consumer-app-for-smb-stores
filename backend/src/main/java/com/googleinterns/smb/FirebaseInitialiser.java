package com.googleinterns.smb;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;


public class FirebaseInitialiser {

    private Firestore db;


    public FirebaseInitialiser() {
        Firestore db = FirestoreOptions.getDefaultInstance().getService();

        this.db = db;
    }


    Firestore getDb() {
        return db;
    }

    public List<CartItem> deleteItem(String userID, String itemName)
            throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userID).collection("cart_items").whereEqualTo("item_name", itemName).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        
        List<CartItem> deletedItems = new ArrayList<>();

        for (QueryDocumentSnapshot document: documents){
            String itemDocID = document.getId();
            DocumentReference docRef = db.collection("cart").document(userID).collection("cart_items").document(itemDocID);
            ApiFuture<WriteResult> futureWrite = docRef.delete();
            deletedItems.add(docRef.get().get().toObject(CartItem.class));
        }

        return deletedItems;

    }

    public List<CartItem> updateQuantity(String userID, String itemName, int quantity) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userID).collection("cart_items").whereEqualTo("item_name", itemName).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        
        List<CartItem> updatedItems = new ArrayList<>();

        for (QueryDocumentSnapshot document: documents){
           
            String itemDocID = document.getId();
            DocumentReference docRef = db.collection("cart").document(userID).collection("cart_items").document(itemDocID);

            ApiFuture<WriteResult> futureWrite = docRef.update("quantity", quantity);

            updatedItems.add(docRef.get().get().toObject(CartItem.class));

        }

        return updatedItems;

    }

    public ArrayList<CartItem> retrieveCartItems(String userId) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userId).collection("cart_items").get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        ArrayList<CartItem> allItems = new ArrayList<>();

        for (QueryDocumentSnapshot doc: documents){
            CartItem item = doc.toObject(CartItem.class);
            allItems.add(item);
        }
        

        return allItems;
    }


    public ArrayList<OrderDocuments> retrieveOrderDetails(String userID) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection("order").whereEqualTo("user_id", userID).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        ArrayList<OrderDocuments> orderDocs = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String docID = document.getId();
            Order order = document.toObject(Order.class);
            OrderDocuments completeOrder = new OrderDocuments(order);
            ApiFuture<QuerySnapshot> itemFuture = db.collection("order").document(docID).collection("items").get();
            List<QueryDocumentSnapshot> itemDocs = itemFuture.get().getDocuments();
            
            for (QueryDocumentSnapshot doc: itemDocs){
                Item item = doc.toObject(Item.class);
                completeOrder.addItem(item);
            }

            orderDocs.add(completeOrder);
        }

        return orderDocs;

    }


    public CartItem addItemToCart(String userID, CartItem item) throws InterruptedException, ExecutionException {
        DocumentReference docRef = db.collection("cart").document(userID);

        ApiFuture<QuerySnapshot> future = db.collection("cart").document(userID).collection("cart_items").get();
        ApiFuture<DocumentReference> addedDoc = db.collection("cart").document(userID).collection("cart_items").add(item);

        String addedDocId = addedDoc.get().getId();

        return docRef.collection("cart_items").document(addedDocId).get().get().toObject(CartItem.class);
        
    }

    public void addOrderDoc(OrderDocuments orderDoc) throws InterruptedException, ExecutionException {
        ApiFuture<DocumentReference> addedDocRef = db.collection("order").add(orderDoc.getOrder());


        String docID = addedDocRef.get().getId();
        List<Item> allItems = orderDoc.getAllItems();

        for (Item item: allItems){
            ApiFuture<DocumentReference> addedSubDocRef = db.collection("order").document(docID).collection("items").add(item);  
        }
    }

}