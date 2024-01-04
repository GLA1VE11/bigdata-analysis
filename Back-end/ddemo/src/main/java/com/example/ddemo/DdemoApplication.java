package com.example.ddemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.ddemo.mapper")
public class DdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdemoApplication.class, args);
    }

}
