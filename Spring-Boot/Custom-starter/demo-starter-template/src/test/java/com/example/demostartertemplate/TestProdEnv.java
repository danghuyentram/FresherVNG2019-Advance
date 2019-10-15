package com.example.demostartertemplate;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class TestProdEnv {
    @Value("${person.name}")
    private String name;

    @Bean
    public String getName(){
        return name;
    }

    @Test
    public void checkProdEnv(){
        // GIVEN

        // WHEN

        // THEN
        Assert.assertEquals("product",getName());
    }
}
