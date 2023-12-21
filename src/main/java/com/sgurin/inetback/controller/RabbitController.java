package com.sgurin.inetback.controller;

import com.sgurin.inetback.model.rabbit.MessageModel;
import com.sgurin.inetback.service.rabbit.RabbitMQProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    private final RabbitMQProducerService rabbitMQProducerService;

    @Autowired
    public RabbitController(RabbitMQProducerService rabbitMQProducerService) {
        this.rabbitMQProducerService = rabbitMQProducerService;
    }

    /*
      curl --location --request GET 'localhost:8002/rabbit/send' \
      --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxLHNndXJpbi5kZXZAZ21haWwuY29tIiwiaXNzIjoiU0d1cmluIiwicm9sZXMiOiJbUk9MRV9BRE1JTiwgUk9MRV9VU0VSXSIsImlhdCI6MTcwMzE1NjQyMSwiZXhwIjoxNzAzMjQyODIxfQ.k47RnKEtuNyo8p-3vGMr56huMa2cJompoFLlN6-UqtR7os_EsZ4Y6LmrfSWHc8AiIxAWI1TQsJGlsA8qLpPBZA' \
      --header 'Content-Type: application/json' \
      --data '{
          "message": "just text",
          "routingKey": "testRoutingKey"
      }'
    * */
    @GetMapping("/send")
    public void sendMessageToRabbit(@RequestBody MessageModel messageModel) {
        rabbitMQProducerService.sendMessage(messageModel.getMessage(), messageModel.getRoutingKey());
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
