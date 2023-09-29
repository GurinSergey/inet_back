package com.sgurin.inetback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
public class InetBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(InetBackApplication.class, args);
    }

    @PostConstruct
    public void init() {
        // Setting Spring Boot SetTimeZone
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}

//https://www.codejava.net/frameworks/spring-boot/spring-security-jwt-authentication-tutorial
//https://www.codejava.net/frameworks/spring-boot/spring-security-jwt-role-based-authorization
