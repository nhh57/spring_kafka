package com.example.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ProducerApp {

    private static final Logger log = LoggerFactory.getLogger(ProducerApp.class);

    public static void main(String[] args) throws InterruptedException {
        String topic = "replicated-topic";

        try (MessageProducerService producerService = new MessageProducerService();
             Scanner scanner = new Scanner(System.in)) {

            log.info("Producer service started.");
            log.info("Sending sample messages to topic '{}'.", topic);

            for (int i = 0; i < 5; i++) {
                String message = "Hello Kafka Message " + i;
                producerService.sendMessage(topic, String.valueOf(i), message);
                Thread.sleep(100); // Small delay to simulate real-world sending
            }
            log.info("Finished sending sample messages.");
        } catch (Exception e) {
            log.error("An error occurred in the producer application", e);
        }
        log.info("Producer application finished.");
    }
}