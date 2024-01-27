package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.JsMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.kafkaworkspace2.model.Topic.JS_JSON_TOPIC;

@Component
@Slf4j
public class JsConsumer {
    @KafkaListener(
            topics = { JS_JSON_TOPIC },
            groupId = "js-consumer-group"
    )
    public void accept(ConsumerRecord<String, JsMessage> message) {
        log.info("[json consumer] - " + message.value());
    }
}
