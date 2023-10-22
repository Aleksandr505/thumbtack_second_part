package com.gladun.buscompany;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ConfigurationProperties(prefix="spring.datasource")
@MapperScan("com.gladun.buscompany.mappers")
public class BuscompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuscompanyApplication.class, args);
    }

}
