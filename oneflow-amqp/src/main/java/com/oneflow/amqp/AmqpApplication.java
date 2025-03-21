package com.oneflow.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.oneflow.**")
public class AmqpApplication {
    public static void main(String[] args) {
        SpringApplication.run(AmqpApplication.class, args);
    }
}