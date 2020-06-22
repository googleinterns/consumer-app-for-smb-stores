package com.googleinterns.smb;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
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

        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH).whereEqualTo("user_id", userId)
                .orderBy("order_id", Query.Direction.DESCENDING).get();
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

    public List<OrderDocuments> getOngoingOrders(String userId) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH).whereEqualTo("user_id", userId)
                .whereEqualTo("order_status", "ONGOING").orderBy("order_id", Query.Direction.DESCENDING).get();
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

    public void updateOrderDoc(OrderDocuments orderDoc) throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH)
                .whereEqualTo("order_id", orderDoc.getOrder().getOrderId()).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        for (QueryDocumentSnapshot document : documents) {

            HashMap<String, Object> updates = new HashMap<>();
            String orderDocID = document.getId();
            DocumentReference docRef = db.collection(ORDER_PATH).document(orderDocID);
            updates.put("order_status", "PROCESSING");
            updates.put("merchant_id", orderDoc.getOrder().getMerchantId());
            updates.put("merchant_name", orderDoc.getOrder().getMerchantName());
            updates.put("merchant_address", orderDoc.getOrder().getMerchantAddress());
            updates.put("offers_availed", orderDoc.getOrder().getOffersAvailed());

            docRef.update(updates);

            for (Item item : orderDoc.getItemDetails()) {
                ApiFuture<QuerySnapshot> itemFuture = db.collection(ORDER_PATH).document(orderDocID).collection("items")
                        .whereEqualTo("item_name", item.getItemName()).get();

                QuerySnapshot itemQuerySnapshot = itemFuture.get();
                List<QueryDocumentSnapshot> itemDocuments = itemQuerySnapshot.getDocuments();

                for (QueryDocumentSnapshot queryDocumentSnapshot : itemDocuments) {
                    String itemDocID = queryDocumentSnapshot.getId();
                    DocumentReference documentReference = db.collection(ORDER_PATH).document(orderDocID)
                            .collection("items").document(itemDocID);

                    HashMap<String, Object> itemUpdates = new HashMap<>();
                    itemUpdates.put("quantity", item.getQuantity());
                    itemUpdates.put("unit_price", item.getUnitPrice());

                    documentReference.update(itemUpdates);
                }

            }
        }

    }

    public List<Item> getItemsForOrder(String orderId) throws InterruptedException, ExecutionException {
        ApiFuture<QuerySnapshot> future = db.collection(ORDER_PATH).whereEqualTo("order_id", orderId).get();
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

        List<Item> itemsInOrder = new ArrayList<>();

        for (QueryDocumentSnapshot document : documents) {
            String docId = document.getId();
            ApiFuture<QuerySnapshot> itemFuture = db.collection(ORDER_PATH).document(docId).collection("items").get();
            List<QueryDocumentSnapshot> itemDocs = itemFuture.get().getDocuments();

            for (QueryDocumentSnapshot doc : itemDocs) {
                Item item = doc.toObject(Item.class);
                itemsInOrder.add(item);
            }
        }

        return itemsInOrder;

    }
}