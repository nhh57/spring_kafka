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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class SimpleStreamProcessor {

    private static final Logger log = LoggerFactory.getLogger(SimpleStreamProcessor.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "simple-stream-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // Start reading from the beginning of the topic

        final StreamsBuilder builder = new StreamsBuilder();

        // 2.4: Tạo KStream từ một topic đầu vào
        KStream<String, String> sourceStream = builder.stream("input-topic", Consumed.with(Serdes.String(), Serdes.String()));

        // 2.5: Áp dụng phép biến đổi filter(): Giữ lại các tin nhắn có giá trị chứa "important"
        KStream<String, String> filteredStream = sourceStream.filter((key, value) -> {
            log.info("Filtering - Key: {}, Value: {}", key, value);
            return value.contains("important");
        });

        // 2.6: Áp dụng phép biến đổi map(): Chuyển đổi giá trị tin nhắn thành chữ hoa
        KStream<String, String> mappedStream = filteredStream.mapValues(value -> {
            log.info("Mapping - Value: {} to {}", value, value.toUpperCase());
            return value.toUpperCase();
        });

        // 2.7: Áp dụng phép biến đổi groupByKey() và count(): Đếm số lượng tin nhắn theo key
        KTable<String, Long> countedStream = mappedStream.groupByKey(Grouped.with(Serdes.String(), Serdes.String()))
                                                    .count(Materialized.as("counts-store"));

        // 2.8: Ghi kết quả KTable (từ count) ra một topic đầu ra
        countedStream.toStream().to("output-topic-counts", Produced.with(Serdes.String(), Serdes.Long()));

        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            log.error("Error starting Kafka Streams application", e);
            System.exit(1);
        }
    }
}