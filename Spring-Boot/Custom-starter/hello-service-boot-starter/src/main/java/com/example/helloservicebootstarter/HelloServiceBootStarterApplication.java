package com.example.helloservicebootstarter;

import com.example.helloservicebootstarter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelloServiceBootStarterApplication {
//	@Autowired
//	HelloService helloService;

	public static void main(String[] args) {
		SpringApplication.run(HelloServiceBootStarterApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		helloService.sayHello();
//	}
}
