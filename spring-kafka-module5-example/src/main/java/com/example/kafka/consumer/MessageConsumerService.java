package com.example.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {

    @KafkaListener(topics = "${spring.kafka.consumer.topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message, Acknowledgment acknowledgment) {
        System.out.println("Received Message in group '" + "${spring.kafka.consumer.group-id}" + "': " + message);
        try {
            // Simulate an error for testing Dead Letter Queue
            if (message.contains("error")) {
                throw new RuntimeException("Simulated error processing message: " + message);
            }
            // Acknowledge the message only after successful processing
            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error processing message: " + message + " - " + e.getMessage());
            // Do not acknowledge here, let the error handler handle retries/DLT
            throw e;
        }
    }
}