package com.example.kafkaworkspace2.consumer;

import com.example.kafkaworkspace2.model.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.List;
import static com.example.kafkaworkspace2.model.Topic.JS_JSON_TOPIC;

@Component
@Slf4j
public class BatchConsumer {

    @KafkaListener(
            topics = { JS_JSON_TOPIC },
            groupId = "batch-consumer-group",
            containerFactory = "batchKafkaListenerContainerFactory"
    )
    public void accept(List<ConsumerRecord<String, Data>> messages) {
        log.info("[Batch Consumer] Batch message arrived! - count " + messages.size());
        messages.forEach(message -> {
                    log.info("[Batch Consumer] Value - " + message.value() + " / Offset - " + message.offset() + " / Partition - " + message.partition());
                }
        );
    }
}
