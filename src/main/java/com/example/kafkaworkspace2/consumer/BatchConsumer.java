package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.JsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.example.kafkaworkspace2.model.Topic.JS_STRING_TOPIC;

@Component
@Slf4j
public class BatchConsumer {

    @KafkaListener(
            topics = {JS_STRING_TOPIC},
            groupId = "batch-consumer-group",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    public void accept(List<ConsumerRecord<String, String>> messages) {
        log.info("[Batch Consumer] Batch message arrived! - count " + messages.size());
        ObjectMapper objectMapper = new ObjectMapper();
        messages.forEach(message -> {
                    JsMessage deserialMessage;
                    try {
                        deserialMessage = objectMapper.readValue(message.value(), JsMessage.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("[Batch Consumer] Value - " + deserialMessage + " / Offset - " + message.offset() + " / Partition - " + message.partition());
                }
        );
    }
}
