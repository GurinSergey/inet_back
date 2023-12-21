package com.sgurin.inetback.component.rabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@EnableRabbit
public class RabbitMQConsumer {

    @RabbitListener(queues = "queue1")
    public void processMyQueue(String message) {
        Date currentDate = new Date();
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate);

        System.out.printf("%s : received from myQueue : %s ", formattedDate, new String(message.getBytes()));
        System.out.println();
    }
}