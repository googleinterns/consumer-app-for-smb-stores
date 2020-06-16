package com.googleinterns.smb;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;

public class FirebaseHandler {
         static String convertInputStreamToString(InputStream inputStream)
                throws IOException {

                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                }

                return result.toString(StandardCharsets.UTF_8.name());

        }
        public FirebaseHandler() throws IOException {
                InputStream service= FirebaseHandler.class
                .getClassLoader().getResourceAsStream("./service_account_pk.json");
                //FileInputStream serviceAccount = new FileInputStream("./service_account_pk.json");

                Map<String, Object> auth = new HashMap<String, Object>();
                auth.put("uid", "my-service-worker");

                FirebaseOptions options = new FirebaseOptions.Builder()
                                .setCredentials(GoogleCredentials.fromStream(service))
                                .setDatabaseUrl("https://kirana-g.firebaseio.com").build();

                FirebaseApp.initializeApp(options);

        }
}
