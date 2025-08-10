package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicProducer {
    private static final Logger log = LoggerFactory.getLogger(BasicProducer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

        // Synchronous send
        try {
            String topic = "my-sync-topic";
            String message = "Hello, Kafka (sync)!";
            RecordMetadata metadata = producer.send(new ProducerRecord<>(topic, message)).get();
            log.info("Sent synchronously: {} to topic {}, partition {}, offset {}", message, metadata.topic(), metadata.partition(), metadata.offset());
        } catch (Exception e) {
            log.error("Error sending message synchronously", e);
        }

        // Asynchronous send
        String topic = "my-async-topic";
        String message = "Hello, Kafka (async)!";
        producer.send(new ProducerRecord<>(topic, "key1", message), (metadata, exception) -> {
            if (exception == null) {
                log.info("Sent asynchronously: {} to topic {}, partition {}, offset {}", message, metadata.topic(), metadata.partition(), metadata.offset());
            } else {
                log.error("Error sending message asynchronously", exception);
            }
        });

        producer.close();
    }
}