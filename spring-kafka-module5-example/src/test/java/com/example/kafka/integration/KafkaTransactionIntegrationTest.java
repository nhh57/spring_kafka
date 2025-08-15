package com.example.kafka.integration;

import com.example.kafka.producer.MessageProducerService;
import com.example.kafka.consumer.TransactionalCtPService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"transactional-source-topic", "transactional-target-topic", "transactional-source-topic.DLT"})
class KafkaTransactionIntegrationTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private TransactionalCtPService transactionalCtPService;

    private KafkaTemplate<String, String> kafkaTemplate;
    private Consumer<String, String> consumer;

    @BeforeEach
    void setUp() {
        // Configure transactional producer
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-producer-test-id");
        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
        pf.setTransactionIdPrefix("tx-test-"); // Required for transactional producer
        kafkaTemplate = new KafkaTemplate<>(pf);
        kafkaTemplate.setDefaultTopic("transactional-source-topic");
        // kafkaTemplate.setProducerFactory(pf); // This line is not needed and causes compilation error

        // Configure consumer for target topic
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group-tx", "true", embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed"); // Important for transactional reads
        consumer = new DefaultKafkaConsumerFactory<String, String>(consumerProps).createConsumer();
        consumer.subscribe(Collections.singletonList("transactional-target-topic"));
    }

    @AfterEach
    void tearDown() {
        if (consumer != null) {
            consumer.close();
        }
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }

    @Test
    void testTransactionalConsumeAndProduce() throws InterruptedException {
        String inputMessage = "transactional-message-1";
        String expectedOutputMessage = "processed-" + inputMessage;

        // Send message to source topic
        kafkaTemplate.send("transactional-source-topic", "key1", inputMessage);
        kafkaTemplate.flush();

        // Allow some time for the transactional consumer to process
        TimeUnit.SECONDS.sleep(5);

        // Verify message in target topic
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
        assertFalse(records.isEmpty());
        assertEquals(1, records.count());
        assertEquals(expectedOutputMessage, records.iterator().next().value());
    }

    @Test
    void testTransactionalConsumeAndProduceWithError() throws InterruptedException {
        String inputMessage = "transactional-error-message"; // This will trigger an error in CtP service
        
        // Send message to source topic
        kafkaTemplate.send("transactional-source-topic", "key2", inputMessage);
        kafkaTemplate.flush();

        // Allow some time for the transactional consumer to process
        TimeUnit.SECONDS.sleep(5);

        // Verify no message in target topic (transaction should be rolled back)
        ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));
        assertTrue(records.isEmpty());

        // Verify message in DLT
        Consumer<String, String> dltConsumer = new DefaultKafkaConsumerFactory<String, String>(
            KafkaTestUtils.consumerProps("dlt-group-tx", "true", embeddedKafkaBroker)
        ).createConsumer();
        dltConsumer.subscribe(Collections.singletonList("transactional-source-topic.DLT"));
        ConsumerRecords<String, String> dltRecords = KafkaTestUtils.getRecords(dltConsumer, Duration.ofSeconds(5));
        assertFalse(dltRecords.isEmpty());
        assertEquals(1, dltRecords.count());
        assertTrue(dltRecords.iterator().next().value().contains(inputMessage));
        dltConsumer.close();
    }
}