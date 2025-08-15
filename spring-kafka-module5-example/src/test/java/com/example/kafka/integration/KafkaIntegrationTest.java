package com.example.kafka.integration;

// package com.example.kafka.integration; // Commented out to fix compilation issue

import com.example.kafka.producer.MessageProducerService;
import com.example.kafka.consumer.MessageConsumerService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext // Ensures a clean application context for each test, useful with EmbeddedKafka
@EmbeddedKafka(partitions = 1, topics = {"my-topic", "my-topic.DLT"})
class KafkaIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private MessageProducerService producerService;

    @Autowired
    private MessageConsumerService consumerService; // Autowire the actual consumer service

    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Explicitly create DefaultKafkaConsumerFactory with String, String types
        consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(Collections.singletonList("my-topic.DLT")); // Subscribe to DLT for error testing
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
    }

    @Test
    void testProducerAndConsumerFlow() throws InterruptedException {
        String topic = "my-topic";
        String key = "test-key";
        String message = "Hello Kafka!";

        // Send message
        producerService.sendMessage(topic, key, message);

        // Allow some time for the consumer to process the message
        TimeUnit.SECONDS.sleep(2);

        // Manually poll from embedded Kafka to verify message
        Map<String, Object> consumerProps2 = KafkaTestUtils.consumerProps("test-group-2", "true", embeddedKafkaBroker);
        consumerProps2.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps2.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps2.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        try (Consumer<String, String> testConsumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps2).createConsumer()) {
            testConsumer.subscribe(Collections.singletonList(topic));
            ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(testConsumer, Duration.ofSeconds(5));
            assertFalse(records.isEmpty());
            records.forEach(record -> {
                assertNotNull(record.key());
                assertNotNull(record.value());
            });
        }
    }

    @Test
    void testErrorHandlingAndDLT() throws InterruptedException {
        String topic = "my-topic";
        String key = "error-key";
        String errorMessage = "This message contains error"; // This will trigger the simulated error in MessageConsumerService

        // Send message that will cause an error
        producerService.sendMessage(topic, key, errorMessage);

        // Allow some time for the consumer to process the message and for DLT to receive it
        TimeUnit.SECONDS.sleep(5);

        // Poll DLT to verify error message arrived
        ConsumerRecords<String, String> dltRecords = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertFalse(dltRecords.isEmpty());
        assertTrue(dltRecords.iterator().next().value().contains(errorMessage));
    }
}