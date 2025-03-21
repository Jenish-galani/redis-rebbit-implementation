package com.example.student.controller;

import com.example.student.config.MQConfig;
import com.example.student.rabbitmq.CustomMessage;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessagedate(new Date()); // ✅ Fixed issue

        template.convertAndSend(MQConfig.EXCHANGE,
                MQConfig.ROUTING_KEY, message); // ✅ Fixed typo

        return "Message Published";
    }
}
