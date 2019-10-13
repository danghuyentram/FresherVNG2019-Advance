package com.example.demostartertemplate.service;

import com.example.helloservicebootstarter.service.HelloService;


public class CustomHelloService implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("Hello from custom service");
    }
}
