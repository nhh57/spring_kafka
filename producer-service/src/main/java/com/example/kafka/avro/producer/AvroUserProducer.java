package com.example.kafka.avro.producer;

import com.example.kafka.avro.User;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AvroUserProducer {

    private static final Logger log = LoggerFactory.getLogger(AvroUserProducer.class);

    private final Producer<String, User> producer;

    public AvroUserProducer(String bootstrapServers, String schemaRegistryUrl) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", schemaRegistryUrl);

        this.producer = new KafkaProducer<>(props);
    }

    public void sendUserMessage(User user, String topic) {
        ProducerRecord<String, User> record = new ProducerRecord<>(topic, user.getId().toString(), user);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                log.info("Gửi tin nhắn Avro thành công! Metadata: Topic = {}, Partition = {}, Offset = {}, Timestamp = {}",
                         metadata.topic(), metadata.partition(), metadata.offset(), metadata.timestamp());
            } else {
                log.error("Lỗi khi gửi tin nhắn Avro: {}", exception.getMessage());
            }
        });
    }

    public void close() {
        producer.close();
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Cách dùng: AvroUserProducer <bootstrap.servers> <schema.registry.url>");
            return;
        }

        String bootstrapServers = args[0];
        String schemaRegistryUrl = args[1];
        String topic = "users-avro-topic"; // Tên topic

        AvroUserProducer producer = new AvroUserProducer(bootstrapServers, schemaRegistryUrl);

        try {
            // Tạo một đối tượng User từ lớp Avro đã tạo
            User user1 = User.newBuilder()
                    .setId("user123")
                    .setName("Nguyen Van A")
                    .setEmail("nguyenvana@example.com")
                    .build();

            User user2 = User.newBuilder()
                    .setId("user456")
                    .setName("Tran Thi B")
                    .setEmail("tranthib@example.com")
                    .build();

            producer.sendUserMessage(user1, topic);
            producer.sendUserMessage(user2, topic);

            log.info("Đã gửi các tin nhắn User Avro.");

        } finally {
            producer.close();
            log.info("Producer đã đóng.");
        }
    }
}