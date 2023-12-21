package com.sgurin.inetback.model.rabbit;

import lombok.Data;

@Data
public class MessageModel {
    private String message;
    private String routingKey;
}
