package com.example.kafkaworkspace2.api;

import com.example.kafkaworkspace2.model.JsMessage;
import com.example.kafkaworkspace2.producer.JsProducer;
import com.example.kafkaworkspace2.producer.JsProducer2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class Controller {
    private final JsProducer jsProducer;
    private final JsProducer2 jsProducer2;


    @RequestMapping("/hello")
    String hello() {
        return "Hello World";
    }

    @PostMapping("/message")
    void message(@RequestBody JsMessage message) {
        jsProducer.sendMessage(message);
    }

    @PostMapping("/message/{key}")
    void message(@PathVariable String key, @RequestBody String message) {
        jsProducer2.sendMessageWithKey(key, message);
    }
}
