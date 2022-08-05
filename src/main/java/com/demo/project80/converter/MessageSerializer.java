package com.demo.project80.converter;

import com.demo.project80.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Serializer;

public class MessageSerializer implements Serializer<User> {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public byte[] serialize(String topic, User data) {
        return mapper.writeValueAsBytes(data);
    }
}
