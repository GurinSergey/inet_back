package com.sgurin.inetback.controller;

import com.sgurin.inetback.model.chatgpt.ChatBotRequest;
import com.sgurin.inetback.model.chatgpt.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/chatgpt")
public class ChatBotController {
    private final RestTemplate restTemplate;

    @Value("${openai.chatgtp.model}")
    private String model;

    @Value("${openai.chatgtp.max-completions}")
    private int maxCompletions;

    @Value("${openai.chatgtp.temperature}")
    private double temperature;

    @Value("${openai.chatgtp.max_tokens}")
    private int maxTokens;

    @Value("${openai.chatgtp.api.url}")
    private String apiUrl;

    @Autowired
    public ChatBotController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/send")
    public ChatBotRequest chat(@RequestParam("prompt") String prompt) {
        ChatBotRequest request = new ChatBotRequest(model,
                Collections.singletonList(new Message("user", prompt)),
                maxCompletions,
                temperature,
                maxTokens);

        ChatBotRequest response = restTemplate.postForObject(apiUrl, request, ChatBotRequest.class);
        return response;
    }
}