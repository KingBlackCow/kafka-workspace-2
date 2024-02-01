package com.example.kafkaworkspace2.service;

import com.example.kafkaworkspace2.data.JsEntity;
import com.example.kafkaworkspace2.data.JsJpaRepository;
import com.example.kafkaworkspace2.model.JsConverter;
import com.example.kafkaworkspace2.model.JsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class JsServiceImpl implements JsService {

    private final JsJpaRepository jsJpaRepository;

    @Override
    public List<JsDTO> findAll() {
        List<JsEntity> entities = jsJpaRepository.findAll();
        return entities.stream().map(JsConverter::toDTO).toList();
    }

    @Override
    public JsDTO findById(Integer id) {
        Optional<JsEntity> entity = jsJpaRepository.findById(id);
        return entity.map(JsConverter::toDTO).orElse(null);
    }

    @Override
    public JsDTO save(JsDTO model) {
        JsEntity entity = jsJpaRepository.save(JsConverter.toEntity(model));
        return JsConverter.toDTO(entity);
    }

    @Override
    public void delete(Integer id) {
        jsJpaRepository.deleteById(id);
    }
}
