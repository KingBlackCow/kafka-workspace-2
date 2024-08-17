package com.example.kafkaworkspacejava.model;

import lombok.Data;

@Data
public class Message {
    private int id;
    private int age;
    private String name;
    private String content;
}
