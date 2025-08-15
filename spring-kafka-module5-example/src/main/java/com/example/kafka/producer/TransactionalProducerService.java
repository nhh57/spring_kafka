package com.example.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionalProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional("kafkaTransactionManager") // Use the Kafka transaction manager bean
    public void sendTransactionalMessage(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message);
        System.out.println("Transactional message sent to topic " + topic + ": " + message);
        // Simulate an error to test transaction rollback
        if (message.contains("rollback")) {
            throw new RuntimeException("Simulating rollback for message: " + message);
        }
    }
}