package com.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ExchangesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExchangesApplication.class, args);
    }

}
