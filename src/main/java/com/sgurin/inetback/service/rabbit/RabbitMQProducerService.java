package com.sgurin.inetback.service.rabbit;

public interface RabbitMQProducerService {
    void sendMessage(String message, String routingKey);
}
