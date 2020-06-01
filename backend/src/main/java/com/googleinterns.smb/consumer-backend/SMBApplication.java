package com.googleinterns.smb;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
@RestController
@SpringBootApplication
<<<<<<< HEAD:backend/src/main/java/com/googleinterns.smb/consumer-backend/SMBApplication.java
public class SMBApplication {
=======
public class SMBKiranaApplication {
>>>>>>> a4d78c478aedc8db67c62642a7f8002bef85cc35:Practice2/src/main/java/com/example/Practice2/Practice2Application.java

	public static void main(String[] args)
	{
		SpringApplication.run(Practice2Application.class,args);
	}
	@RequestMapping("/about")
	public Map<String,String> hello()
	{
		HashMap<String,String> temp=new HashMap<>();
		temp.put("A","B");
		return temp;
	}
}

