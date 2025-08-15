package com.example.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalCtPService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionalCtPService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "transactional-source-topic", groupId = "transactional-group", containerFactory = "kafkaListenerContainerFactory")
    @Transactional("kafkaTransactionManager")
    public void consumeAndProduce(String message, Acknowledgment acknowledgment) {
        System.out.println("Received message in transactional consumer: " + message);

        try {
            // Simulate processing
            String processedMessage = "processed-" + message;

            // Simulate error for testing DLT and transaction rollback
            if (message.contains("transactional-error-message")) {
                throw new RuntimeException("Simulated error during transactional processing.");
            }

            // Produce message to another topic within the same transaction
            kafkaTemplate.send("transactional-target-topic", processedMessage);
            System.out.println("Produced message: " + processedMessage + " to transactional-target-topic");

            // Acknowledge the message only after successful processing and producing
            acknowledgment.acknowledge();
        } catch (Exception e) {
            System.err.println("Error in transactional consume and produce: " + e.getMessage());
            // Re-throw to trigger transaction rollback and DLT processing
            throw e;
        }
    }
}