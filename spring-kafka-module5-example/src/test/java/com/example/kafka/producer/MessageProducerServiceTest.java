package com.example.kafka.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessageProducerServiceTest {

    private MessageProducerService messageProducerService;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        messageProducerService = new MessageProducerService(kafkaTemplate);
    }

    @Test
    void sendMessage_shouldCallKafkaTemplateSend() {
        String topic = "test-topic";
        String key = "test-key";
        String message = "test-message";
        messageProducerService.sendMessage(topic, key, message);
        verify(kafkaTemplate).send(topic, key, message);
    }
}