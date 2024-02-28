package com.sgurin.inetback;

import com.sgurin.inetback.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@EnableAspectJAutoProxy
@EnableConfigurationProperties(AppProperties.class)
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
//https://www.callicoder.com/spring-boot-security-oauth2-social-login-part-1/
