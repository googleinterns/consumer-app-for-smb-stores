package com.googleinterns.smb;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.*;
import java.io.InputStream;

public class FirebaseHandler {
         
        public FirebaseHandler() throws IOException {
                InputStream serviceAccount= FirebaseHandler.class
                .getClassLoader().getResourceAsStream("./service_account_pk.json");
                FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .setDatabaseUrl("https://kirana-g.firebaseio.com").build();

                FirebaseApp.initializeApp(options);

        }
}
