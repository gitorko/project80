package com.demo.project80.producer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.demo.project80.pojo.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class KafkaProducer {

    @Value(value = "${topic.name}")
    private String topicName;

    private final KafkaTemplate<String, User> kafkaTemplate;

    @Bean
    public CommandLineRunner sendData() {
        return args -> {
            List<String> users = Arrays.asList("david", "john", "raj", "peter");
            Random random = new Random();
            while (true) {
                User user = new User(users.get(random.nextInt(users.size())), random.nextInt(100));
                ProducerRecord<String, User> producerRecord = new ProducerRecord<>(topicName, user);
                producerRecord.headers().add("message-id", UUID.randomUUID().toString().getBytes());
                log.info("Sending user: {}", user);
                kafkaTemplate.send(producerRecord);
                TimeUnit.SECONDS.sleep(10);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaProducer.class, args);
    }

}
