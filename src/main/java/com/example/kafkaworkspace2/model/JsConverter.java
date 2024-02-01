package com.example.kafkaworkspace2.model;

import com.example.kafkaworkspace2.data.JsEntity;

public class JsConverter {

    public static JsDTO toDTO(JsEntity entity) {
        return new JsDTO(
            entity.getId(),
            entity.getUserId(),
            entity.getUserAge(),
            entity.getUserName(),
            entity.getContent(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public static JsEntity toEntity(JsDTO model) {
        return new JsEntity(
            model.getId(),
            model.getUserId(),
            model.getUserAge(),
            model.getUserName(),
            model.getContent(),
            model.getCreatedAt(),
            model.getUpdatedAt()
        );
    }

    public static JsDTO toDTO(JsCdcMessage message) {
        return new JsDTO(
            message.getId(),
            message.getPayload().getUserId(),
            message.getPayload().getUserAge(),
            message.getPayload().getUserName(),
            message.getPayload().getContent(),
            message.getPayload().getCreatedAt(),
            message.getPayload().getUpdatedAt()
        );
    }

    public static JsCdcMessage toMessage(Integer id, JsDTO model, OperationType operationType) {
        JsCdcMessage.Payload payload = null;
        if (operationType == OperationType.CREATE || operationType == OperationType.UPDATE) { // C, U의 경우만 payload 존재
            payload = new JsCdcMessage.Payload(
                model.getUserId(),
                model.getUserAge(),
                model.getUserName(),
                model.getContent(),
                model.getCreatedAt(),
                model.getUpdatedAt()
            );
        }
        return new JsCdcMessage(id, payload, operationType);
    }
}
