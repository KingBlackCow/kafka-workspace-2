package com.example.kafkaworkspacejava.api;

import com.example.kafkaworkspacejava.model.UserDTO;
import com.example.kafkaworkspacejava.model.Message;
import com.example.kafkaworkspacejava.producer.JsonProducer;
import com.example.kafkaworkspacejava.producer.ObjectMapperProducer;
import com.example.kafkaworkspacejava.producer.StringProducer;
import com.example.kafkaworkspacejava.service.UserService;
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
    private final UserService userService;

    @RequestMapping("/hello")
    String hello() {
        return "Hello World";
    }

    @PostMapping("/message")
    void message(@RequestBody Message message) throws JsonProcessingException {
        jsonProducer.sendMessage(message);
    }

    @PostMapping("/message/{key}")
    void message(@PathVariable String key, @RequestBody String message) {
        stringProducer.sendMessageWithKey(key, message);
    }

    @PostMapping("/message/json")
    void messageJson(@RequestBody Message message) throws JsonProcessingException {
        objectMapperProducer.sendMessage(message);
    }

    @PostMapping("/greetings")
    UserDTO create(@RequestBody Request request) throws JsonProcessingException {
        if (request == null || request.userId == null || request.userName == null || request.userAge == null || request.content == null)
            return null;

        UserDTO userDTO = UserDTO.create(request.userId, request.userAge, request.userName, request.content);
        return userService.save(userDTO);
    }

    @GetMapping("/greetings")
    List<UserDTO> list() {
        return userService.findAll();
    }

    @GetMapping("/greetings/{id}")
    UserDTO get(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @PatchMapping("/greetings/{id}")
    UserDTO update(@PathVariable Integer id, @RequestBody String content) throws JsonProcessingException {
        if (id == null || content == null || content.isBlank()) return null;
        UserDTO userDTO = userService.findById(id);
        userDTO.setContent(content);
        return userService.save(userDTO);
    }

    @DeleteMapping("/greetings/{id}")
    void delete(@PathVariable Integer id) throws JsonProcessingException {
        userService.delete(id);
    }

    @Data
    private static class Request {
        Integer userId;
        String userName;
        Integer userAge;
        String content;
    }
}
