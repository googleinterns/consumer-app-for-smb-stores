package com.googleinterns.smb;
import java.util.*;
import java.io.*;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SMBKiranaApplication {

	public static FirebaseInitialiser fireStoreInstance;

	public static void main(String[] args) throws IOException{
		FirebaseInitialiser quickStart = new FirebaseInitialiser();
		fireStoreInstance = quickStart;

		firebaseHandler handler = new firebaseHandler();
		
		SpringApplication.run(SMBKiranaApplication.class, args);
	}


}

