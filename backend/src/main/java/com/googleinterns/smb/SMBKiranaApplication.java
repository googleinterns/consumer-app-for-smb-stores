package com.googleinterns.smb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SMBKiranaApplication {

    public static void main(String[] args) throws IOException {
        new FirebaseHandler();
        SpringApplication.run(SMBKiranaApplication.class, args);
    }
}
