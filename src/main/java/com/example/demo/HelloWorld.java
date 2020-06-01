package com.example.demo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloWorld {
//    @RequestMapping("/hello")
//    public String hello()
//    {
//        return "Hello Ichha :)";
//    }
    @RequestMapping("/about")
    public Map<String,String> hello2()
    {
        HashMap<String,String> temp=new HashMap<>();
        temp.put("A","B");
        return temp;
    }
}
