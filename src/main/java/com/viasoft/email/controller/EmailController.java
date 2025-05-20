package com.viasoft.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viasoft.email.dto.EmailDTO;
import com.viasoft.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    private final Environment environment;
    private final EmailService emailService;
    private final ObjectMapper objectMapper;

    @Autowired
    public EmailController(Environment environment, EmailService emailService, ObjectMapper objectMapper) {
        this.environment = environment;
        this.emailService = emailService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/enviar-email")
    public ResponseEntity<Void> enviarEmail(@RequestBody @jakarta.validation.Valid EmailDTO emailDTO) throws JsonProcessingException {
        String integracao = environment.getProperty("mail.integracao");
        Object emailAdaptado = emailService.adaptarEmailParaIntegracao(emailDTO, integracao);

        String json = objectMapper.writeValueAsString(emailAdaptado);
        System.out.println("JSON do objeto adaptado: " + json);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}