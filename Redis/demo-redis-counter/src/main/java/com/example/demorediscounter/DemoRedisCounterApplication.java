package com.example.demorediscounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoRedisCounterApplication  {
	public static void main(String[] args) {
		SpringApplication.run(DemoRedisCounterApplication.class, args);
	}

}
