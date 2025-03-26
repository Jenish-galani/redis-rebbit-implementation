package com.example.student.controllers;

import com.example.student.controller.MessagePublisher;
import com.example.student.rabbitmq.CustomMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(MessagePublisher.class)
class MessagePublisherTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublishMessage() throws Exception {
        String requestBody = "{\"message\":\"Test Message\", \"messageId\":\"\", \"messagedate\":\"\"}";

        Mockito.doNothing().when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(CustomMessage.class));

        mockMvc.perform(post("/publish")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().string("Message Published"));
    }
}

