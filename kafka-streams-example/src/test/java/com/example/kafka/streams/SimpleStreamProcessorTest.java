package com.example.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimpleStreamProcessorTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> filteredAndMappedOutputTopic;
    private TestOutputTopic<String, Long> countedOutputTopic;

    private static final String INPUT_TOPIC = "input-topic";
    private static final String FILTERED_MAPPED_TOPIC = "filtered-mapped-output-topic";
    private static final String COUNTED_TOPIC = "output-topic-counts";

    @BeforeEach
    void setup() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-simple-stream-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> sourceStream = builder.stream(INPUT_TOPIC, Consumed.with(Serdes.String(), Serdes.String()));

        KStream<String, String> filteredStream = sourceStream.filter((key, value) -> value.contains("important"));

        KStream<String, String> mappedStream = filteredStream.mapValues(value -> value.toUpperCase());

        // Output for filtered and mapped stream (optional, for testing intermediate steps)
        mappedStream.to(FILTERED_MAPPED_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        KTable<String, Long> countedStream = mappedStream.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                                                    .count(Materialized.as("counts-store"));

        countedStream.toStream().to(COUNTED_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

        testDriver = new TopologyTestDriver(builder.build(), props);
        inputTopic = testDriver.createInputTopic(INPUT_TOPIC, Serdes.String().serializer(), Serdes.String().serializer());
        filteredAndMappedOutputTopic = testDriver.createOutputTopic(FILTERED_MAPPED_TOPIC, Serdes.String().deserializer(), Serdes.String().deserializer());
        countedOutputTopic = testDriver.createOutputTopic(COUNTED_TOPIC, Serdes.String().deserializer(), Serdes.Long().deserializer());
    }

    @AfterEach
    void tearDown() {
        testDriver.close();
    }

    @Test
    void testFilterAndMap() {
        // Test case for filter() and map()
        inputTopic.pipeInput("key1", "this is an important message");
        inputTopic.pipeInput("key2", "this is a regular message");
        inputTopic.pipeInput("key3", "another important one");

        assertEquals("THIS IS AN IMPORTANT MESSAGE", filteredAndMappedOutputTopic.readValue());
        assertEquals("ANOTHER IMPORTANT ONE", filteredAndMappedOutputTopic.readValue());
        
        // Ensure no more messages are in this output topic
        assertNull(filteredAndMappedOutputTopic.readRecord());
    }

    @Test
    void testCount() {
        // Test case for groupByKey() and count()
        inputTopic.pipeInput("keyA", "important message one");
        inputTopic.pipeInput("keyB", "another important message");
        inputTopic.pipeInput("keyA", "important message two"); // Same key as keyA
        inputTopic.pipeInput("keyC", "a third important message");

        // The count results are emitted as updates to the KTable.
        // We need to read all emitted records for each key.
        // For "keyA", the first message makes its count 1. The second makes it 2.
        // For "keyB", its count is 1.
        // For "keyC", its count is 1.

        // Read all records until no more are available
        // Note: TopologyTestDriver might emit intermediate updates for KTable.
        // The final value for a key is the most recent one.

        // After "keyA", "important message one"
        assertEquals("KEYA", countedOutputTopic.readKey());
        assertEquals(1L, countedOutputTopic.readValue());

        // After "keyB", "another important message"
        assertEquals("KEYB", countedOutputTopic.readKey());
        assertEquals(1L, countedOutputTopic.readValue());

        // After "keyA", "important message two"
        assertEquals("KEYA", countedOutputTopic.readKey());
        assertEquals(2L, countedOutputTopic.readValue());

        // After "keyC", "a third important message"
        assertEquals("KEYC", countedOutputTopic.readKey());
        assertEquals(1L, countedOutputTopic.readValue());

        assertNull(countedOutputTopic.readRecord());
    }

    @Test
    void testNonImportantMessagesAreFiltered() {
        inputTopic.pipeInput("key1", "regular message");
        inputTopic.pipeInput("key2", "another regular message");

        assertNull(filteredAndMappedOutputTopic.readRecord());
        assertNull(countedOutputTopic.readRecord());
    }
}