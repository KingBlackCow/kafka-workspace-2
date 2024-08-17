package com.example.kafkaworkspacejava.consumer;

import com.example.kafkaworkspacejava.common.CustomObjectMapper;
import com.example.kafkaworkspacejava.model.UserCdcMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.kafkaworkspacejava.model.Topic.JS_CDC_TOPIC;

@Component
@Slf4j
public class CdcConsumer {

    private final CustomObjectMapper objectMapper = new CustomObjectMapper();
    private int retryCount = 0;

    @KafkaListener(
        topics = { JS_CDC_TOPIC },
        groupId = "cdc-consumer-group",
        concurrency = "3"
    )
    public void listen(ConsumerRecord<String, String> message, Acknowledgment acknowledgment) throws JsonProcessingException {
        String retryPrint = retryCount != 0 ? "(Retry: " + retryCount + ")": "";
        UserCdcMessage userCdcMessage = objectMapper.readValue(message.value(), UserCdcMessage.class);
        log.info(retryPrint + "[Cdc Consumer] " + userCdcMessage.getOperationType() + " Message arrived! (id: " + userCdcMessage.getId() + ") - " + userCdcMessage.getPayload() + " / time: " + LocalDateTime.now());
        retryCount++;
        throw new RuntimeException("Someting happend");
//        acknowledgment.acknowledge();
    }
}
