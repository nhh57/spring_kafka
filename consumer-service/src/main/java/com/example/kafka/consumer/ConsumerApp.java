package com.example.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerApp {

    private static final Logger log = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        String topic = "replicated-topic";
        String groupId = "learning-group";

        MessageConsumerService consumerService = new MessageConsumerService(topic, groupId);
        Thread consumerThread = new Thread(consumerService);
        consumerThread.start();

        // Add a shutdown hook to gracefully close the consumer service
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Shutdown hook triggered. Closing consumer service...");
            consumerService.close();
            try {
                consumerThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("Consumer service closed.");
        }));
    }
}