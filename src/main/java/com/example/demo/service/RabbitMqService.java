package com.example.demo.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class RabbitMqService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitMqService(RabbitTemplate rabbitTemplate, RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void sendAudioToQueue(byte[] audioData) {
        String queueName = "audio_queue";
        rabbitTemplate.convertAndSend(queueName, audioData);
        System.out.println("Аудиофайл успешно отправлен в очередь.");
    }

    @RabbitListener(queues = "audio_output_queue")
    public void getAudioFromQueue(Message message){
        String file_name = "this_file";
        byte[] arr = message.getBody();
        try {
            FileOutputStream outputStream = new FileOutputStream(file_name);
            assert arr != null;
            outputStream.write(arr);
            outputStream.close();
            System.out.println("done");

        } catch (IOException exception){
            System.out.println("not");
        }
    }
}