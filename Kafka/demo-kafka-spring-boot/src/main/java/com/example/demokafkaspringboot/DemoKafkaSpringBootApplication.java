package com.example.demokafkaspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
//@EnableKafka
public class DemoKafkaSpringBootApplication  {

	public static void main(String[] args) {
		SpringApplication.run(DemoKafkaSpringBootApplication.class, args);
	}

}
