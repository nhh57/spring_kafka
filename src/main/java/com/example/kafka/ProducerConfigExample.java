package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerConfigExample {

    private static final Logger log = LoggerFactory.getLogger(ProducerConfigExample.class);

    public static void main(String[] args) {
        // Common properties
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // --- acks=0 ---
        log.info("--- Testing with acks=0 ---");
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        sendMessages(props, "my-topic-acks-0");

        // --- acks=1 ---
        log.info("--- Testing with acks=1 ---");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        sendMessages(props, "my-topic-acks-1");

        // --- acks=all ---
        log.info("--- Testing with acks=all ---");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        // For acks=all, enable.idempotence is highly recommended
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        sendMessages(props, "my-topic-acks-all");

        // --- retries and idempotence ---
        log.info("--- Testing with retries and idempotence ---");
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // Must be 'all' for idempotence
        props.put(ProducerConfig.RETRIES_CONFIG, 3); // Number of retries
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000); // Backoff time between retries
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // Enable idempotence
        sendMessages(props, "my-topic-retries-idempotence");
    }

    private static void sendMessages(Properties producerProps, String topic) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(producerProps)) {
            String message = "Test message for " + topic;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "key", message);

            try {
                RecordMetadata metadata = producer.send(record).get(); // Synchronous send for simplicity
                log.info("Successfully sent message to topic: {}, partition: {}, offset: {}",
                         metadata.topic(), metadata.partition(), metadata.offset());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error sending message to {}: {}", topic, e.getMessage());
            }
        }
    }
}