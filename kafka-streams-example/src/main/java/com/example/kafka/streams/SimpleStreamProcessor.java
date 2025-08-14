package com.example.kafka.streams;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.Topology; // Add this line
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class SimpleStreamProcessor {

    private static final Logger log = LoggerFactory.getLogger(SimpleStreamProcessor.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-stream-processor-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Assuming Kafka is running on localhost
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start reading from the beginning if no offset is found

        SimpleStreamProcessor processor = new SimpleStreamProcessor();
        Topology topology = processor.buildTopology();

        // 2.9: Build and start Kafka Streams application
        KafkaStreams streams = new KafkaStreams(topology, props);

        // Clean up local state on shutdown. ONLY for development/testing.
        // In production, handle state carefully.
        streams.cleanUp();

        streams.start();

        // Add shutdown hook to close the Streams application gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        log.info("Kafka Streams application started.");
    }

    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        // 2.4: Create KStream from an input topic
        KStream<String, String> sourceStream = builder.stream(
                "input-topic",
                Consumed.with(Serdes.String(), Serdes.String())
        );

        // 2.5: Apply filter(): Keep messages whose value contains "important"
        KStream<String, String> filteredStream = sourceStream.filter((key, value) -> {
            log.info("Filtering: Key={}, Value={}", key, value);
            return value.contains("important");
        });

        // 2.6: Apply map(): Convert message value to uppercase
        KStream<String, String> mappedStream = filteredStream.mapValues(value -> {
            log.info("Mapping to uppercase: {}", value);
            return value.toUpperCase();
        });

        // 2.7: Apply groupByKey() and count(): Count messages by key
        KTable<String, Long> countedStream = mappedStream
                .groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                .count(Materialized.as("counts-store")); // State store for counts

        // Output the mapped stream for testing filter and map
        mappedStream.to(
                "filtered-mapped-output-topic",
                Produced.with(Serdes.String(), Serdes.String())
        );

        // 2.8: Write KTable (from count) to an output topic
        countedStream.toStream().to(
                "output-topic-counts",
                Produced.with(Serdes.String(), Serdes.Long())
        );

        return builder.build();
    }
}