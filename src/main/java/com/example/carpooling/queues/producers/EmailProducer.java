package com.example.carpooling.queues.producers;

import com.example.carpooling.dto.EmailDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailDto emailDto){
        rabbitTemplate.convertAndSend("email-queue",emailDto);
    }
}
