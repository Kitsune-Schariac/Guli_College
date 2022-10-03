package com.kitsune.staservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@ComponentScan(basePackages = {"com.kitsune"})
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.kitsune.staservice.mapper")
@EnableScheduling
public class StaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class, args);
    }

}
