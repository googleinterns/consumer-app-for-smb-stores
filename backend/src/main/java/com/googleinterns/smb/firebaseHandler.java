package com.googleinterns.smb;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class firebaseHandler {

        Merchant merchant;

        public firebaseHandler() throws IOException {

                FileInputStream serviceAccount = new FileInputStream("./service_account_pk.json");

                Map<String, Object> auth = new HashMap<String, Object>();
                auth.put("uid", "my-service-worker");

                FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .setDatabaseUrl("https://kirana-g.firebaseio.com").build();

                FirebaseApp.initializeApp(options);

                DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("restricted_access/secret_document");
                ref.addListenerForSingleValueEvent((ValueEventListener) new ValueEventListener() {
                        // @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                                Object document = dataSnapshot.getValue();
                                System.out.println(document);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                });

        }
}
