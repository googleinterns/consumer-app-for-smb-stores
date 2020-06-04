package com.googleinterns.smb;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SMBKiranaApplication {

	public static FirebaseInitialiser fireStoreInstance;

	public static void main(String[] args) {
		FirebaseInitialiser quickStart = new FirebaseInitialiser();
		fireStoreInstance = quickStart;
		SpringApplication.run(SMBKiranaApplication.class, args);
	}


}

