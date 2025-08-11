package com.example.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ProducerApp {

    private static final Logger log = LoggerFactory.getLogger(ProducerApp.class);

    public static void main(String[] args) {
        String topic = "learning-topic-0";

        try (MessageProducerService producerService = new MessageProducerService();
             Scanner scanner = new Scanner(System.in)) {

            log.info("Producer service started.");
            log.info("Enter messages to send to topic '{}'. Type 'exit' to quit.", topic);

            while (true) {
                System.out.print("message> ");
                String line = scanner.nextLine();
                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }
                producerService.sendMessage(topic, line, line);
            }
        } catch (Exception e) {
            log.error("An error occurred in the producer application", e);
        }
        log.info("Producer application finished.");
    }
}