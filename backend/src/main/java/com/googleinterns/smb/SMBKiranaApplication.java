package com.googleinterns.smb;


import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;

@SpringBootApplication
public class SMBKiranaApplication {

	public static void main(String[] args) throws IOException {

		firebaseHandler handler = new firebaseHandler();

		SpringApplication.run(SMBKiranaApplication.class, args);
	}
}
