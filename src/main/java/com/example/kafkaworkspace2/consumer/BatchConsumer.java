package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.JsMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.kafkaworkspace2.model.Topic.JS_STRING_TOPIC;

@Component
@Slf4j
public class BatchConsumer {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @KafkaListener(
            topics = {JS_STRING_TOPIC},
            groupId = "batch-consumer-group",
            containerFactory = "batchKafkaListenerContainerFactory",
            concurrency = "3"
    )
    public void accept(List<ConsumerRecord<String, String>> messages, Acknowledgment acknowledgment) {
        log.info("[Batch Consumer] Batch message arrived! - count " + messages.size());
        ObjectMapper objectMapper = new ObjectMapper();
        messages.forEach(message -> executorService.submit(() -> {
            {
                JsMessage deserializedMessage;
                try {
                    deserializedMessage = objectMapper.readValue(message.value(), JsMessage.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                log.info("[Batch Consumer(" + Thread.currentThread().getId() + ")] Value - " + deserializedMessage + " / Offset - " + message.offset() + " / Partition - " + message.partition());
            }
        }));
        acknowledgment.acknowledge();
    }
}
