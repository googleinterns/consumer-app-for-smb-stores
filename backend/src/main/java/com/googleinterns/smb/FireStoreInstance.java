package com.googleinterns.smb;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;

@Component
public class FireStoreInstance {

    private Firestore db;
    private static final String ORDER_PATH = "order";

    public FireStoreInstance() {
        this.db = FirestoreOptions.getDefaultInstance().getService();
    }


    public List<OrderDocuments> retrieveOrderDetails(String userId) throws InterruptedException, ExecutionException {

        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH).whereEqualTo("user_id", userId).orderBy("order_id", Query.Direction.DESCENDING).get();
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


    public void addOrderDoc(OrderDocuments orderDoc) throws InterruptedException, ExecutionException {
        ApiFuture<DocumentReference> addedDocRef = db.collection(ORDER_PATH).add(orderDoc.getOrder());

        String docID = addedDocRef.get().getId();
        List<Item> allItems = orderDoc.getItemDetails();

        for (Item item : allItems) {
            db.collection(ORDER_PATH).document(docID).collection("items").add(item);
        }
    }
}