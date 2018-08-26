package com.zyx.jshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zyx.jshop.mapper")
public class JshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(JshopApplication.class, args);
    }
}
