package com.example.kafkaworkspace2.config;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.ExponentialBackOff;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.kafkaworkspace2.model.Topic.JS_CDC_CUSTOM_TOPIC_DLT;

@Configuration
@EnableKafka
public class JsonKafkaConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.kafka.string")
    public KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    @Primary
    public KafkaTemplate<String, ?> kafkaTemplate(KafkaProperties kafkaProperties) {
        return new KafkaTemplate<>(producerFactory(kafkaProperties));
    }

    @Bean
    @Primary
    public ProducerFactory<String, Object> producerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getKeySerializer());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProperties.getProducer().getValueSerializer());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "false"); // eos 설정, spring.kafka.json.producer.acks: -1 설정필요
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    @Primary
    public ConsumerFactory<String, Object> consumerFactory(KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        // 수동 커밋 설정
         props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return new DefaultKafkaConsumerFactory<>(props);
    }

//    @Bean
//    @Primary
//    CommonErrorHandler errorHandler() {
//        CommonContainerStoppingErrorHandler cseh = new CommonContainerStoppingErrorHandler();
//        AtomicReference<Consumer<? ,?>> consumer2 = new AtomicReference<>();
//        AtomicReference<MessageListenerContainer> container2 = new AtomicReference<>();
//
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler((rec, ex) -> {
//            cseh.handleRemaining(ex, Collections.singletonList(rec), consumer2.get(), container2.get());
//        }, generateBackOff()) {
//
//            @Override
//            public void handleRemaining(
//                    Exception thrownException,
//                    List<ConsumerRecord<?, ?>> records,
//                    Consumer<?, ?> consumer,
//                    MessageListenerContainer container
//            ) {
//                consumer2.set(consumer);
//                container2.set(container);
//                super.handleRemaining(thrownException, records, consumer, container);
//            }
//        };
//        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
//        return errorHandler;
//    }

    @Bean
    @Primary
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory(
            ConsumerFactory<String, Object> consumerFactory,
            KafkaTemplate<String, Object> kafkaTemplate
//            CommonErrorHandler errorHandler
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        // 컨슈머에서 정의
//        factory.setConcurrency(1);

        /**
         * 에러핸들러 설정 (default 0.5초간격으로 9번 추가 실행)
         * FixedBackOff(간격, 몇번리트라이):
         */

//        DefaultErrorHandler errorHandler = new DefaultErrorHandler();
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 2L));
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 2L));
//        DefaultErrorHandler errorHandler = new DefaultErrorHandler(generateBackOff());
//        /**
//         * addNotRetryableExceptions: IllegalArgumentException이 발생하면 Retry하지 않겠다.
//         */
//        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);
//
//        factory.setCommonErrorHandler(errorHandler);

        /**
         * CommonContainerStoppingErrorHandler: 에러발생시 컨슈마 중지
         */
//        factory.setCommonErrorHandler(new CommonContainerStoppingErrorHandler());

        /**
         * 커스텀 에러 핸들러: 위의 errorHandler 빈 확인
         */
//        factory.setCommonErrorHandler(errorHandler);

        /**
         * DeadLetterPublishingRecoverer: 컨슘에 실패한 메시지를 다른 큐에 메시지를 따로 보관하는 방식
         */
        //factory.setCommonErrorHandler(new DefaultErrorHandler(new DeadLetterPublishingRecoverer(kafkaTemplate), generateBackOff()));

        // 수동 DLT 설정
        factory.setCommonErrorHandler(new DefaultErrorHandler((record, exception) -> {
            kafkaTemplate.send(JS_CDC_CUSTOM_TOPIC_DLT, (String) record.key(), record.value());
        }, generateBackOff()));

        // 수동 커밋 설정
         factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private BackOff generateBackOff() {
        ExponentialBackOff backOff = new ExponentialBackOff(1000, 2);
//        backOff.setMaxAttempts(1); // 최대 1번만 시도
        backOff.setMaxElapsedTime(10000); // 최대 10초까지만 증가
        return backOff;
    }
}
