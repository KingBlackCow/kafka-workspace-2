package com.example.kafkaworkspacejava.model;

import com.example.kafkaworkspacejava.data.User;

public class UserConverter {

    public static UserDTO toDTO(User entity) {
        return new UserDTO(
            entity.getId(),
            entity.getUserId(),
            entity.getUserAge(),
            entity.getUserName(),
            entity.getContent(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static User toEntity(UserDTO model) {
        return new User(
            model.getId(),
            model.getUserId(),
            model.getUserAge(),
            model.getUserName(),
            model.getContent(),
            model.getCreatedAt(),
            model.getUpdatedAt()
        );
    }

    public static UserDTO toDTO(UserCdcMessage message) {
        return new UserDTO(
            message.getId(),
            message.getPayload().getUserId(),
            message.getPayload().getUserAge(),
            message.getPayload().getUserName(),
            message.getPayload().getContent(),
            message.getPayload().getCreatedAt(),
            message.getPayload().getUpdatedAt()
        );
    }

    public static UserCdcMessage toMessage(Integer id, UserDTO model, OperationType operationType) {
        UserCdcMessage.Payload payload = null;
        if (operationType == OperationType.CREATE || operationType == OperationType.UPDATE) { // C, U의 경우만 payload 존재
            payload = new UserCdcMessage.Payload(
                model.getUserId(),
                model.getUserAge(),
                model.getUserName(),
                model.getContent(),
                model.getCreatedAt(),
                model.getUpdatedAt()
            );
        }
        return new UserCdcMessage(id, payload, operationType);
    }
}
