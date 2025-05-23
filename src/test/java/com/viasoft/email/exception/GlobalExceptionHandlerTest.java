package com.viasoft.email.exception;

import com.viasoft.email.controller.FakeErrorController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FakeErrorController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar 400 com mensagem de argumento inválido")
    void shouldHandleIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test-errors/illegal-arg"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Tipo de integração desconhecido: UNKNOWN"));
    }

    @Test
    @DisplayName("Deve retornar 400 com mensagem de ConstraintViolation")
    void shouldHandleConstraintViolationException() throws Exception {
        mockMvc.perform(get("/test-errors/constraint"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Simulated validation error"));
    }

    @Test
    @DisplayName("Deve retornar 400 com mensagem de IllegalArgumentException simulando MethodArgumentNotValid")
    void shouldHandleMethodArgumentExceptionAsFallback() throws Exception {
        mockMvc.perform(get("/test-errors/method-arg"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Campo obrigatório não informado."));
    }

    @Test
    @DisplayName("Deve retornar 500 para exceções genéricas")
    void shouldHandleGenericException() throws Exception {
        mockMvc.perform(get("/test-errors/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.errors[0]").value("Erro inesperado."));
    }

    @Test
    @DisplayName("Resposta deve conter timestamp e status")
    void shouldContainStandardErrorFields() throws Exception {
        mockMvc.perform(get("/test-errors/illegal-arg"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors").isArray());
    }
}
