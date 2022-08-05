package com.demo.project80.consumer;

import com.demo.project80.pojo.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@Slf4j
public class KafkaConsumer {

    @SneakyThrows
    @KafkaListener(id = "my-client-app", topics = "${topic.name}")
    public void topicConsumer(ConsumerRecord<String, User> consumerRecord) {
        User user = consumerRecord.value();
        log.info("Received User : {}", user);
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumer.class, args);
    }

}
