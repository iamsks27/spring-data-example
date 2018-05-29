package com.example.springdatadynamo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example"})
@EntityScan(basePackages = {"com.example.models"})
@EnableJpaRepositories(basePackages = {"com.example.jparepository"})
public class SpringDataDynamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataDynamoApplication.class, args);
    }

}
