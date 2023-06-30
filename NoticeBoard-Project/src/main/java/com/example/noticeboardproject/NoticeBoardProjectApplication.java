package com.example.noticeboardproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class NoticeBoardProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoticeBoardProjectApplication.class, args);
    }

}
