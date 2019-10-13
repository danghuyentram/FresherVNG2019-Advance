package com.example.demostartertemplate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")

public class DemoStarterTemplateApplicationTests {

	@Autowired
	ApplicationContext ctx;


	@Value("${person.name}")
	private String name;

	@Autowired
	private ConfigurableEnvironment env;

//	@Test
//	public void checkExistDispatcherServlet(){
////		ApplicationContext ctx = SpringApplication.run(CustomAppApplication.class);
//		assertNotNull(ctx.getBean("dispatcherServlet"));
//	}

//	@Test
//	public void checkDevEnv(){
//		env.setActiveProfiles("dev");
//		Assert.assertEquals(name,"development");
//
//	}

	@Bean
	public String getName(){
		return name;

	}


	@Test
	public void checkProdEnv(){

		Assert.assertEquals("product",getName());

	}






}
