package com.example.customapp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomAppApplicationTests {

    @Autowired
    ApplicationContext ctx;

	@Test
	public void checkExistDispatcherServlet(){
//		ApplicationContext ctx = SpringApplication.run(CustomAppApplication.class);
		assertNotNull(ctx.getBean("dispatcherServlet"));
	}

//	@Test
//	public void checkExistCustomHelloService(){
//		ApplicationContext ctx = SpringApplication.run(CustomAppApplication.class);
//		assertNotNull(ctx.getBean("customHelloService"));
//	}

//
//	@Test
//	public void showBeanInContext(){
//		ApplicationContext ctx = SpringApplication.run(CustomAppApplication.class);
//		String[] beanNames = ctx.getBeanDefinitionNames();
//		System.out.println("Bean list");
//		Arrays.sort(beanNames);
//		for (String beanName : beanNames) {
//			System.out.println(beanName);
//		}
//	}
//
//

}

