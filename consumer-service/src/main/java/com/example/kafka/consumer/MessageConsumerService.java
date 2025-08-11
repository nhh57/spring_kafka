package com.example.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A reusable service for consuming messages from a Kafka topic.
 * This class runs in a separate thread to continuously poll for messages.
 */
public class MessageConsumerService implements Runnable, Closeable {

    private static final Logger log = LoggerFactory.getLogger(MessageConsumerService.class);

    private final KafkaConsumer<String, String> consumer;
    private final String topic;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    /**
     * Constructs a new MessageConsumerService.
     *
     * @param topic The topic to subscribe to.
     * @param groupId The consumer group ID.
     */
    public MessageConsumerService(String topic, String groupId) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // --- Basic Consumer Configuration ---
        // 'enable.auto.commit=false' gives us full control over when to commit offsets.
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 'auto.offset.reset=earliest' ensures the consumer starts from the beginning of the topic if no offset is found.
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
    }


    @Override
    public void run() {
        try {
            consumer.subscribe(Collections.singletonList(topic));
            log.info("MessageConsumerService started. Subscribed to topic: {}", topic);

            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    log.info("Received message: partition={}, offset={}, key={}, value='{}'",
                            record.partition(), record.offset(), record.key(), record.value());
                }
                if (!records.isEmpty()) {
                    consumer.commitSync();
                }

            }
        } catch (WakeupException e) {
            // This exception is expected when shutdown() is called.
            log.info("MessageConsumerService is shutting down...");
        } catch (Exception e) {
            log.error("Unexpected error in MessageConsumerService", e);
        } finally {
            consumer.close();
            log.info("MessageConsumerService consumer closed.");
        }
    }

    /**
     * Initiates a graceful shutdown of the consumer service.
     */
    @Override
    public void close() {
        closed.set(true);
        // The wakeup() method will interrupt the poll() call and cause the consumer to exit its loop.
        consumer.wakeup();
    }
}