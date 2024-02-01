package com.example.kafkaworkspace2.service;


import com.example.kafkaworkspace2.model.JsDTO;

import java.util.List;

public interface JsService {

    public List<JsDTO> findAll();
    public JsDTO findById(Integer id);
    public JsDTO save(JsDTO model);
    public void delete(Integer id);
}
