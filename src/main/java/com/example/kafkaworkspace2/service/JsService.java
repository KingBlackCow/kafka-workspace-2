package com.example.kafkaworkspace2.service;

import com.example.kafkaworkspace2.data.JsEntity;
import com.example.kafkaworkspace2.data.JsJpaRepository;
import com.example.kafkaworkspace2.model.JsConverter;
import com.example.kafkaworkspace2.model.JsDTO;
import com.example.kafkaworkspace2.model.OperationType;
import com.example.kafkaworkspace2.producer.JsCdcProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JsService {

    private final JsJpaRepository jsJpaRepository;

    public List<JsDTO> findAll() {
        List<JsEntity> entities = jsJpaRepository.findAll();
        return entities.stream().map(JsConverter::toDTO).toList();
    }

    public JsDTO findById(Integer id) {
        Optional<JsEntity> entity = jsJpaRepository.findById(id);
        return entity.map(JsConverter::toDTO).orElse(null);
    }

    @Transactional
    public JsDTO save(JsDTO model) {
        OperationType operationType = model.getId() == null ? OperationType.CREATE : OperationType.UPDATE;
        JsEntity entity = jsJpaRepository.save(JsConverter.toEntity(model));
        return JsConverter.toDTO(entity);
    }

    @Transactional
    public void delete(Integer id) {
        jsJpaRepository.deleteById(id);
    }
}
