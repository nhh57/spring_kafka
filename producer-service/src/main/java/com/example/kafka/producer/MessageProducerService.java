package com.example.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Properties;

/**
 * A reusable service for producing messages to Kafka topics.
 * This class encapsulates the KafkaProducer and provides a simple interface for sending messages.
 */
public class MessageProducerService implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(MessageProducerService.class);

    private final KafkaProducer<String, String> producer;

    /**
     * Constructs a new MessageProducerService with a robust configuration.
     */
    public MessageProducerService() {
        Properties props = new Properties();
        // Connect to the Kafka broker on the host machine
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // Use StringSerializer for keys and values
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // --- Basic Producer Configuration ---
        // 'acks=all' ensures the leader waits for all in-sync replicas to acknowledge the message.
        props.put(ProducerConfig.ACKS_CONFIG,"1");
        // 'enable.idempotence=true' prevents duplicate messages from being produced in case of retries.
        // props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true"); // Commented out for acks=1 testing

        this.producer = new KafkaProducer<>(props);
        log.info("MessageProducerService initialized.");
    }

    /**
     * Sends a message asynchronously to a specified Kafka topic.
     *
     * @param topic   The topic to send the message to.
     * @param key     The key for the message, used for partitioning.
     * @param value   The message payload.
     */
    public void sendMessage(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                log.info("Successfully sent message: key={}, value='{}' to topic={}, partition={}, offset={}",
                        key, value, metadata.topic(), metadata.partition(), metadata.offset());
            } else {
                log.error("Failed to send message with key={}: {}", key, exception.getMessage());
            }
        });
    }

    /**
     * Closes the underlying Kafka producer.
     * This should be called when the service is no longer needed to free up resources.
     */
    @Override
    public void close() {
        if (producer != null) {
            log.info("Closing MessageProducerService...");
            producer.flush(); // Ensure all buffered records are sent
            producer.close();
            log.info("MessageProducerService closed.");
        }
    }
}