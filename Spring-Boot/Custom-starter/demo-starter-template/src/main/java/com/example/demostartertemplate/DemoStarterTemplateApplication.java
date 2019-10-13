package com.example.demostartertemplate;

import com.example.demostartertemplate.models.Student;
import com.example.demostartertemplate.service.CustomHelloService;
import com.example.helloservicebootstarter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class DemoStarterTemplateApplication implements CommandLineRunner {
	@Autowired
	HelloService service;

	@Autowired
	Student student;

	@Value("${person.name}")
	private String name;

	public static void main(String[] args) {
		SpringApplication.run(DemoStarterTemplateApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		service.sayHello();
		System.out.println("hello to  "+ name);
		System.out.println("Global variable:");
		System.out.println("\t Email: " + student.getEmail());
		System.out.println("\t GA ID: " + student.getGoogleAnalyticsId());
		System.out.println("\t Authors: " + student.getAuthors());
		System.out.println("\t Example Map: " + student.getExampleMap());
		System.out.println("\t ABC: " + student.getAbc());


	}

	@Bean
	public HelloService helloService(){
		return new CustomHelloService();
	}

}
