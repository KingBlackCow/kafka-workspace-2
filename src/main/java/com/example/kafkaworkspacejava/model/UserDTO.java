package com.example.kafkaworkspacejava.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO {
    private final Integer id;
    private final Integer userId;
    private final Integer userAge;
    private final String userName;
    private String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static UserDTO create(Integer userId, Integer userAge, String userName, String content) {
        return new UserDTO(null, userId, userAge, userName, content, null, null);
    }
}
