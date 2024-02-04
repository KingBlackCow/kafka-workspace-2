package com.example.kafkaworkspace2.data;

import com.example.kafkaworkspace2.model.JsConverter;
import com.example.kafkaworkspace2.model.JsDTO;
import com.example.kafkaworkspace2.model.OperationType;
import com.example.kafkaworkspace2.producer.JsCdcProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JsEntityListener {

    @Lazy
    @Autowired
    private JsCdcProducer jsCdcProducer;

    @PostPersist
    public void handleCreate(JsEntity jsEntity) {
        System.out.println("handleCreate");
        JsDTO jsDTO = JsConverter.toDTO(jsEntity);
        try {
            jsCdcProducer.sendMessage(
                JsConverter.toMessage(
                    jsDTO.getId(),
                    jsDTO,
                    OperationType.CREATE
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostUpdate
    public void handleUpdate(JsEntity jsEntity) {
        log.info("handleUpdate");
        JsDTO jsDTO = JsConverter.toDTO(jsEntity);
        try {
            jsCdcProducer.sendMessage(
                    JsConverter.toMessage(
                    jsDTO.getId(),
                    jsDTO,
                    OperationType.UPDATE
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostRemove
    public void handleDelete(JsEntity myEntity) {
        System.out.println("handleDelete");
        JsDTO myModel = JsConverter.toDTO(myEntity);
        try {
            jsCdcProducer.sendMessage(
                    JsConverter.toMessage(
                    myModel.getId(),
                    null,
                    OperationType.DELETE
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
