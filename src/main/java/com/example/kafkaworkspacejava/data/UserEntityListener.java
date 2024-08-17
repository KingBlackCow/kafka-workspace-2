package com.example.kafkaworkspacejava.data;

import com.example.kafkaworkspacejava.model.UserConverter;
import com.example.kafkaworkspacejava.model.UserDTO;
import com.example.kafkaworkspacejava.model.OperationType;
import com.example.kafkaworkspacejava.producer.UserCdcProducer;
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
public class UserEntityListener {

    @Lazy
    @Autowired
    private UserCdcProducer userCdcProducer;

    @PostPersist
    public void handleCreate(User user) {
        System.out.println("handleCreate");
        UserDTO userDTO = UserConverter.toDTO(user);
        try {
            userCdcProducer.sendMessage(
                UserConverter.toMessage(
                    userDTO.getId(),
                        userDTO,
                    OperationType.CREATE
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostUpdate
    public void handleUpdate(User user) {
        log.info("handleUpdate");
        UserDTO userDTO = UserConverter.toDTO(user);
        try {
            userCdcProducer.sendMessage(
                    UserConverter.toMessage(
                    userDTO.getId(),
                            userDTO,
                    OperationType.UPDATE
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostRemove
    public void handleDelete(User myEntity) {
        System.out.println("handleDelete");
        UserDTO myModel = UserConverter.toDTO(myEntity);
        try {
            userCdcProducer.sendMessage(
                    UserConverter.toMessage(
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
