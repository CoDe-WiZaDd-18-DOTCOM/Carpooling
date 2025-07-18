package com.example.carpooling.queues.producers;

import com.example.carpooling.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmaildlqProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDlq(EmailDto emailDto){
        rabbitTemplate.convertAndSend("email-dlq",emailDto);
    }
}
