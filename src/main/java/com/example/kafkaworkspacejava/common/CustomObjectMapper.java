package com.example.kafkaworkspacejava.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CustomObjectMapper extends ObjectMapper {

    // Payload LocalDateTime 존재하여 ObjectMapper 커스텀
    public CustomObjectMapper() {
        registerModule(new JavaTimeModule());
    }
}
