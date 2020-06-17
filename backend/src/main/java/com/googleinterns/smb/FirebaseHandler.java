package com.googleinterns.smb;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.*;

public class FirebaseHandler {

        public FirebaseHandler() throws IOException {
                FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.getApplicationDefault())
                                .setDatabaseUrl("https://kirana-g.firebaseio.com/").build();

                FirebaseApp.initializeApp(options);

        }
}
