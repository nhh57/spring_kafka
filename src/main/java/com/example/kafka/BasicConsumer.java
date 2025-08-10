package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class BasicConsumer {
    private static final Logger log = LoggerFactory.getLogger(BasicConsumer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "my-consumer-group");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            String topic = "my-first-topic";
            consumer.subscribe(Collections.singletonList(topic));
            log.info("Subscribed to topic: {}", topic);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100)); // Poll every 100ms
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Received message: partition = {}, offset = {}, key = {}, value = {}",
                             record.partition(), record.offset(), record.key(), record.value());
                }
                // Commit offset (will be detailed in US4)
            }
        }
    }
}