package com.example.kafkaworkspace2.api;

import com.example.kafkaworkspace2.model.JsDTO;
import com.example.kafkaworkspace2.model.JsMessage;
import com.example.kafkaworkspace2.producer.JsonProducer;
import com.example.kafkaworkspace2.producer.ObjectMapperProducer;
import com.example.kafkaworkspace2.producer.StringProducer;
import com.example.kafkaworkspace2.service.JsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class Controller {
    private final JsonProducer jsonProducer;
    private final StringProducer stringProducer;
    private final ObjectMapperProducer objectMapperProducer;
    private final JsService jsService;

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

    @PostMapping("/greetings")
    JsDTO create(@RequestBody Request request) throws JsonProcessingException {
        if (request == null || request.userId == null || request.userName == null || request.userAge == null || request.content == null)
            return null;

        JsDTO jsDTO = JsDTO.create(request.userId, request.userAge, request.userName, request.content);
        return jsService.save(jsDTO);
    }

    @GetMapping("/greetings")
    List<JsDTO> list() {
        return jsService.findAll();
    }

    @GetMapping("/greetings/{id}")
    JsDTO get(@PathVariable Integer id) {
        return jsService.findById(id);
    }

    @PatchMapping("/greetings/{id}")
    JsDTO update(@PathVariable Integer id, @RequestBody String content) throws JsonProcessingException {
        if (id == null || content == null || content.isBlank()) return null;
        JsDTO jsDTO = jsService.findById(id);
        jsDTO.setContent(content);
        return jsService.save(jsDTO);
    }

    @DeleteMapping("/greetings/{id}")
    void delete(@PathVariable Integer id) throws JsonProcessingException {
        jsService.delete(id);
    }

    @Data
    private static class Request {
        Integer userId;
        String userName;
        Integer userAge;
        String content;
    }
}
