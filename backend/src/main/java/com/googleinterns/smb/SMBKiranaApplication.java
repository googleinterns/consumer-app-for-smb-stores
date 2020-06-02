package com.googleinterns.smb;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class SMBKiranaApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SMBKiranaApplication.class, args);
	}

	@RequestMapping("/about")
	public Map<String, String> hello() {
		Map<String, String> response = new HashMap<>();
		response.put("key", "value");
		return response;
	}
	
}

