package com.example.kafkaworkspace2.event;

import com.example.kafkaworkspace2.model.JsDTO;
import com.example.kafkaworkspace2.model.OperationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JsCdcApplicationEvent extends ApplicationEvent {

    private final Integer id;
    private final JsDTO jsDTO;
    private final OperationType operationType;

    public JsCdcApplicationEvent(Object source, Integer id, JsDTO jsDTO, OperationType operationType) {
        super(source);
        this.id = id;
        this.jsDTO = jsDTO;
        this.operationType = operationType;
    }
}
