package com.example.kafka.streams;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.TestRecord; // Corrected import
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimpleStreamProcessorTest {

    private TopologyTestDriver testDriver;
    private TestInputTopic<String, String> inputTopic;
    private TestOutputTopic<String, String> filteredAndMappedOutputTopic;
    private TestOutputTopic<String, Long> countedOutputTopic;

    @BeforeEach
    void setup() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-simple-stream-processor-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        Topology topology = new SimpleStreamProcessor().buildTopology(); // Assuming SimpleStreamProcessor has a public buildTopology method
        testDriver = new TopologyTestDriver(topology, props);

        inputTopic = testDriver.createInputTopic("input-topic", Serdes.String().serializer(), Serdes.String().serializer());
        filteredAndMappedOutputTopic = testDriver.createOutputTopic("filtered-mapped-output-topic", Serdes.String().deserializer(), Serdes.String().deserializer());
        countedOutputTopic = testDriver.createOutputTopic("output-topic-counts", Serdes.String().deserializer(), Serdes.Long().deserializer());
    }

    @AfterEach
    void tearDown() {
        if (testDriver != null) {
            testDriver.close();
        }
    }

    @Test
    @DisplayName("Should filter messages not containing 'important'")
    void testFilter() {
        inputTopic.pipeInput("key1", "this is an important message");
        inputTopic.pipeInput("key2", "this is a normal message");
        inputTopic.pipeInput("key3", "another important one");

        // Only "important" messages should pass through the filter
        assertEquals(2, filteredAndMappedOutputTopic.readKeyValuesToList().size());
    }

    @Test
    @DisplayName("Should convert message values to uppercase")
    void testMapValues() {
        inputTopic.pipeInput("key1", "this is an important message");
        TestRecord<String, String> record = filteredAndMappedOutputTopic.readRecord();
        assertEquals("THIS IS AN IMPORTANT MESSAGE", record.value());
    }

    @Test
    @DisplayName("Should count messages by key correctly")
    void testCountByKey() {
        // Pipe inputs
        inputTopic.pipeInput("keyA", "important message one");
        inputTopic.pipeInput("keyB", "another important message");
        inputTopic.pipeInput("keyA", "important message two");
        inputTopic.pipeInput("keyC", "final important message");
        inputTopic.pipeInput("keyA", "important message three");

        // Advance wall-clock time to ensure all records are processed and forwarded
        testDriver.advanceWallClockTime(java.time.Duration.ofSeconds(10));

        // Read all output records from the counted output topic
        // KTable updates are sent as a stream, so read all of them to get the final state
        // We will read them as a list and then manually verify the final counts.
        List<TestRecord<String, Long>> records = countedOutputTopic.readRecordsToList();
        
        // Process the records to get the final count for each key
        Map<String, Long> finalCounts = new HashMap<>();
        for (TestRecord<String, Long> record : records) {
            finalCounts.put(record.getKey(), record.getValue());
        }

        // Assert on the final counts
        assertEquals(3L, finalCounts.get("keyA"));
        assertEquals(1L, finalCounts.get("keyB"));
        assertEquals(1L, finalCounts.get("keyC"));
    }
}