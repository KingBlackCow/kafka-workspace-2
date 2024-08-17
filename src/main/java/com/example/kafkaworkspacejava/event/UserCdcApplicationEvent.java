package com.example.kafkaworkspacejava.event;

import com.example.kafkaworkspacejava.model.UserDTO;
import com.example.kafkaworkspacejava.model.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserCdcApplicationEvent extends ApplicationEvent {

    private final Integer id;
    private final UserDTO userDTO;
    private final OperationType operationType;

    public UserCdcApplicationEvent(Object source, Integer id, UserDTO userDTO, OperationType operationType) {
        super(source);
        this.id = id;
        this.userDTO = userDTO;
        this.operationType = operationType;
    }
}
