package com.example.kafkaworkspace2.api;

import com.example.kafkaworkspace2.model.Data;
import com.example.kafkaworkspace2.producer.JsonProducer;
import com.example.kafkaworkspace2.producer.StringProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class Controller {
    private final JsonProducer jsonProducer;
    private final StringProducer stringProducer;


    @RequestMapping("/hello")
    String hello() {
        return "Hello World";
    }

    @PostMapping("/message")
    void message(@RequestBody Data message) {
        jsonProducer.sendMessage(message);
    }

    @PostMapping("/message/{key}")
    void message(@PathVariable String key, @RequestBody String message) {
        stringProducer.sendMessageWithKey(key, message);
    }
}
