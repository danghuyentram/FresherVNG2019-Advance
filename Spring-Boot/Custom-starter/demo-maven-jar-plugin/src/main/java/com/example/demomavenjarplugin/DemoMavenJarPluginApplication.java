package com.example.demomavenjarplugin;

import com.example.helloservicebootstarter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties

public class DemoMavenJarPluginApplication  implements CommandLineRunner {

	@Autowired
	HelloService service;

	@Value("${person.name}")
	private String name;

	public static void main(String[] args) {
		SpringApplication.run(DemoMavenJarPluginApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		service.sayHello();
		System.out.println("hello to  "+ name);
	}
}
