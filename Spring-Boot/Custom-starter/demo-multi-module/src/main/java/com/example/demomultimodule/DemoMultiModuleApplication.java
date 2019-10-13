package com.example.demomultimodule;

import hello.HelloService;
import hello.HelloServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoMultiModuleApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoMultiModuleApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		HelloService helloService = new HelloServiceImpl();
		helloService.sayHello();
	}
}
