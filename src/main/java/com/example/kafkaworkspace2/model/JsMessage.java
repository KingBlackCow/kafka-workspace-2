package com.example.kafkaworkspace2.model;

import lombok.Data;

@Data
public class JsMessage {
    private int id;
    private int age;
    private String name;
    private String content;
}
