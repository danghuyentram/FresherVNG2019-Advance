package com.example.helloservicebootstarter.service;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("Hello from default starter hello service");
    }
}
