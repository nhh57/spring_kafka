package com.example.kafka.module5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.kafka"}) // Scan base package for configurations and components
@EnableKafka
public class SpringKafkaModule5Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaModule5Application.class, args);
    }

}