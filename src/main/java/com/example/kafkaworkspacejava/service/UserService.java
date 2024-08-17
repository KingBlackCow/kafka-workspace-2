package com.example.kafkaworkspacejava.service;

import com.example.kafkaworkspacejava.data.User;
import com.example.kafkaworkspacejava.data.UserJpaRepository;
import com.example.kafkaworkspacejava.model.UserConverter;
import com.example.kafkaworkspacejava.model.UserDTO;
import com.example.kafkaworkspacejava.model.OperationType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserJpaRepository userJpaRepository;

    public List<UserDTO> findAll() {
        List<User> entities = userJpaRepository.findAll();
        return entities.stream().map(UserConverter::toDTO).toList();
    }

    public UserDTO findById(Integer id) {
        Optional<User> entity = userJpaRepository.findById(id);
        return entity.map(UserConverter::toDTO).orElse(null);
    }

    @Transactional
    public UserDTO save(UserDTO model) {
        OperationType operationType = model.getId() == null ? OperationType.CREATE : OperationType.UPDATE;
        User entity = userJpaRepository.save(UserConverter.toEntity(model));
        return UserConverter.toDTO(entity);
    }

    @Transactional
    public void delete(Integer id) {
        userJpaRepository.deleteById(id);
    }
}
