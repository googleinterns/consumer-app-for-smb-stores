package com.example.Practice2;

import org.springframework.web.bind.annotation.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
@RestController
@SpringBootApplication
public class Practice2Application {

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

