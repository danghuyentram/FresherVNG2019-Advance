package com.example.demostartertemplate.models;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Component
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "student")
@Getter
@Setter
public class Student {
    private String email;
    private String googleAnalyticsId;
    private List<String> authors;
    private Map<String, String> exampleMap;
    private String abc;

}
