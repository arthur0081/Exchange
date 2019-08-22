package com.slabs.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.math.BigDecimal;

@SpringBootApplication
//@EnableScheduling
public class ExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class, args);
    }
}
