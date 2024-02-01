package com.example.kafkaworkspace2.api;

import com.example.kafkaworkspace2.model.JsMessage;
import com.example.kafkaworkspace2.producer.JsonProducer;
import com.example.kafkaworkspace2.producer.ObjectMapperProducer;
import com.example.kafkaworkspace2.producer.StringProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class Controller {
    private final JsonProducer jsonProducer;
    private final StringProducer stringProducer;
    private final ObjectMapperProducer objectMapperProducer;

    @RequestMapping("/hello")
    String hello() {
        return "Hello World";
    }

    @PostMapping("/message")
    void message(@RequestBody JsMessage message) throws JsonProcessingException {
        jsonProducer.sendMessage(message);
    }

    @PostMapping("/message/{key}")
    void message(@PathVariable String key, @RequestBody String message) {
        stringProducer.sendMessageWithKey(key, message);
    }

    @PostMapping("/message/json")
    void messageJson(@RequestBody JsMessage message) throws JsonProcessingException {
        objectMapperProducer.sendMessage(message);
    }
}
