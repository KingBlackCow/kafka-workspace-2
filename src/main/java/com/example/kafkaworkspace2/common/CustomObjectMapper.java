package com.example.kafkaworkspace2.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper extends ObjectMapper {

    // Payload에 LocalDateTime이 존재하여 ObjectMapper를 커스텀
    public CustomObjectMapper() {
        registerModule(new JavaTimeModule());
    }
}
