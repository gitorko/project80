package com.demo.project80.converter;

import com.demo.project80.pojo.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.kafka.common.serialization.Deserializer;

public class MessageDeserializer implements Deserializer<User> {

    private final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public User deserialize(String topic, byte[] data) {
        return mapper.readValue(data, User.class);
    }

}
