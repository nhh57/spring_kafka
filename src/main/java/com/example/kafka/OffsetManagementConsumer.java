package com.example.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.consumer.OffsetAndMetadata; // Corrected import
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

public class OffsetManagementConsumer {
    private static final Logger log = LoggerFactory.getLogger(OffsetManagementConsumer.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "my-consumer-group");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("enable.auto.commit", "false"); // Disable auto-commit

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            String topic = "my-first-topic";
            consumer.subscribe(Collections.singletonList(topic));
            log.info("Subscribed to topic: {}", topic);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    log.info("Received message: partition = {}, offset = {}, key = {}, value = {}",
                             record.partition(), record.offset(), record.key(), record.value());
                }

                if (!records.isEmpty()) {
                    // Synchronous commit
                    try {
                        consumer.commitSync();
                        log.info("Offset committed synchronously.");
                    } catch (Exception e) { // Catching generic Exception
                        log.error("Error committing offset synchronously: {}", e.getMessage());
                        // Handle commit error (e.g., log, exit application)
                    }

                    // Asynchronous commit
                    consumer.commitAsync(new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                            if (exception != null) {
                                log.error("Error committing offset asynchronously: {}", exception.getMessage());
                                // Handle asynchronous commit error
                            } else {
                                log.info("Offset committed successfully: {}", offsets);
                            }
                        }
                    });
                }
            }
        }
    }
}