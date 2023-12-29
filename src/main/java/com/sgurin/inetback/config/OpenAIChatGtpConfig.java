package com.sgurin.inetback.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//https://levelup.gitconnected.com/chatgtp-integration-with-spring-boot-application-280637e69e73
@Configuration
public class OpenAIChatGtpConfig {
    @Value("${openai.chatgtp.api.key}")
    private String openaiApiKey;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}