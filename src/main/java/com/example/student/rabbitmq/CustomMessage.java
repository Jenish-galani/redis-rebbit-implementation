package com.example.student.rabbitmq;

import lombok.Data;
import java.util.Date;

@Data
public class CustomMessage {

    private String messageId;
    private String message;
    private Date messagedate;
}
