package com.demo.project80.stream;

import java.util.Properties;

import com.demo.project80.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;

@SpringBootApplication
@Slf4j
public class KafkaStream {

    @Value(value = "${topic.name}")
    private String topicName;

    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Bean
    public CommandLineRunner streamData() {
        return args -> {
            StreamsBuilder streamsBuilder = new StreamsBuilder();
            KStream<String, User> streamOfUsers = streamsBuilder
                    .stream(topicName, Consumed.with(STRING_SERDE, new JsonSerde<>(User.class)));

            streamOfUsers.foreach((k, v) -> {
                log.info("user: {}, age: {}", v.getName(), v.getAge());
            });

            KTable<String, Long> employeeCountByCompany = streamOfUsers
                    .map((k, v) -> new KeyValue<>(v.getAge(), String.valueOf(v.getAge())))
                    .groupBy((k, w) -> w, Grouped.with(STRING_SERDE, STRING_SERDE))
                    .count();
            employeeCountByCompany.toStream().foreach((w, c) -> log.info("Age: " + w + " -> " + c));

            KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), kStreamsConfigs());
            streams.cleanUp();
            streams.start();
            Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
        };
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public Properties kStreamsConfigs() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "test-group");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return props;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaStream.class, args);
    }

}
