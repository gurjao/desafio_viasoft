package com.viasoft.email.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/test-errors")
public class FakeErrorController {

    @GetMapping("/method-arg")
    public void throwMethodArgument() {
        throw new IllegalArgumentException("Campo obrigatório não informado.");
    }

    @GetMapping("/constraint")
    public void throwConstraintViolation() {
        throw new ConstraintViolationException("Simulated validation error", null);
    }

    @GetMapping("/illegal-arg")
    public void throwIllegalArgument() {
        throw new IllegalArgumentException("Tipo de integração desconhecido: UNKNOWN");
    }

    @GetMapping("/generic")
    public void throwGeneric() {
        throw new RuntimeException("Explodiu");
    }
}