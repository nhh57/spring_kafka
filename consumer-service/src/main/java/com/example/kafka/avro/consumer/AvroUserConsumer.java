package com.example.kafka.avro.consumer;

import com.example.kafka.avro.User;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig; // Add this import
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AvroUserConsumer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(AvroUserConsumer.class);

    private final KafkaConsumer<String, User> consumer;
    private final String topic;

    public AvroUserConsumer(String bootstrapServers, String groupId, String schemaRegistryUrl, String topic) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true); // Enable specific Avro reader
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // Manual offset commit
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start reading from earliest if no offset

        this.consumer = new KafkaConsumer<>(props);
        this.topic = topic;
        this.consumer.subscribe(Collections.singletonList(topic));
    }

    @Override
    public void run() {
        try {
            while (true) {
                ConsumerRecords<String, User> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, User> record : records) {
                    log.info("Received Avro message: Key = {}, Value = {}, Topic = {}, Partition = {}, Offset = {}",
                             record.key(), record.value(), record.topic(), record.partition(), record.offset());
                    // Process the User object
                    User user = record.value();
                    log.info("Processed User: ID = {}, Name = {}, Email = {}", user.getId(), user.getName(), user.getEmail());
                }
                consumer.commitSync(); // Commit offsets manually
            }
        } catch (Exception e) {
            log.error("Error in Avro consumer: {}", e.getMessage(), e);
        } finally {
            consumer.close();
            log.info("Consumer closed.");
        }
    }

    public void close() {
        consumer.close();
    }

    public static void main(String[] args) {
//        if (args.length != 3) {
//            System.out.println("Cách dùng: AvroUserConsumer <bootstrap.servers> <group.id> <schema.registry.url>");
//            return;
//        }

        String bootstrapServers = "10.56.66.54:9092,10.56.66.54:9093";
        String groupId = "users-avro-topic-group";
        String schemaRegistryUrl = "http://10.56.66.54:8081";
        String topic = "users-avro-topic"; // Tên topic

        AvroUserConsumer consumer = new AvroUserConsumer(bootstrapServers, groupId, schemaRegistryUrl, topic);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(consumer::close));
    }
}