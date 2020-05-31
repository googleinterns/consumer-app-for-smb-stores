package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;


@SpringBootApplication

public class DemoApplication 
{
//   @Override
//   protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
//    {
//        return application.sources(DemoApplication.class);
//    }

    public static void main(String[] args)
    {
		SpringApplication.run(DemoApplication.class, args);
	}
//    @RequestMapping("/about")
//    public Map<String,String> hello2()
//    {
//        HashMap<String,String> temp=new HashMap<>();
//        temp.put("A","B");
//        return temp;
//    }

}
