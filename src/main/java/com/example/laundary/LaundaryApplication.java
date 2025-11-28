package com.example.laundary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.laundary", "com.example.laundry"})
public class LaundaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(LaundaryApplication.class, args);
    }
}