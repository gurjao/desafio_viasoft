package com.viasoft.email.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viasoft.email.dto.EmailAwsDTO;
import com.viasoft.email.dto.EmailDTO;
import com.viasoft.email.dto.EmailOciDTO;
import com.viasoft.email.service.EmailService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class) // Testa apenas o EmailController
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc; // Usado para simular requisições HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para converter objetos em JSON

    @MockBean // Cria um mock do EmailService
    private EmailService emailService;

    @MockBean // Cria um mock do Environment
    private Environment environment;

    private EmailDTO createValidEmailDTO() {
        EmailDTO dto = new EmailDTO();
        dto.setDestinatarioEmail("valid@example.com");
        dto.setDestinatarioNome("Valid User");
        dto.setRemetenteEmail("sender@example.com");
        dto.setAssunto("Test Subject");
        dto.setConteudo("Test Content");
        return dto;
    }

    @Test
    @DisplayName("Deve retornar status 204 NO CONTENT ao enviar email com sucesso (AWS)")
    void shouldReturn204WhenSendingEmailSuccessAws() throws Exception {
        EmailDTO emailDTO = createValidEmailDTO();
        EmailAwsDTO awsDTO = new EmailAwsDTO(); // Objeto de retorno simulado
        awsDTO.setRecipient("valid@example.com"); // Preencher com dados mínimos para evitar NullPointerException

        // Simula o comportamento do Environment.getProperty
        when(environment.getProperty("mail.integracao")).thenReturn("AWS");

        // Simula o comportamento do emailService.adaptarEmailParaIntegracao
        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("AWS")))
                .thenReturn(awsDTO);

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        verify(emailService).adaptarEmailParaIntegracao(any(EmailDTO.class), eq("AWS"));
    }

    @Test
    @DisplayName("Deve retornar status 204 NO CONTENT ao enviar email com sucesso (OCI)")
    void shouldReturn204WhenSendingEmailSuccessOci() throws Exception {
        EmailDTO emailDTO = createValidEmailDTO();
        EmailOciDTO ociDTO = new EmailOciDTO(); // Objeto de retorno simulado
        ociDTO.setRecipientEmail("valid@example.com"); // Preencher com dados mínimos

        // Simula o comportamento do Environment.getProperty
        when(environment.getProperty("mail.integracao")).thenReturn("OCI");

        // Simula o comportamento do emailService.adaptarEmailParaIntegracao
        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("OCI")))
                .thenReturn(ociDTO);

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());

        // Verifica se o método do serviço foi chamado corretamente
        verify(emailService).adaptarEmailParaIntegracao(any(EmailDTO.class), eq("OCI"));
    }

    @Test
    @DisplayName("Deve retornar status 400 BAD REQUEST quando o DTO de entrada é inválido")
    void shouldReturn400WhenInputDTOIsInvalid() throws Exception {
        EmailDTO invalidEmailDTO = new EmailDTO(); // DTO inválido intencionalmente
        invalidEmailDTO.setDestinatarioEmail("invalid-email"); // E-mail inválido para forçar erro

        // Não precisamos mockar o serviço aqui, pois a validação @Valid no controller
        // intercepta antes de chamar o serviço se o DTO de entrada for inválido.
        // O Spring já cuida dessa validação.

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 BAD REQUEST quando o serviço lança ConstraintViolationException")
    void shouldReturn400WhenServiceThrowsConstraintViolationException() throws Exception {
        EmailDTO emailDTO = createValidEmailDTO();

        when(environment.getProperty("mail.integracao")).thenReturn("AWS");

        // Simula o serviço lançando uma exceção de validação (como se o DTO adaptado fosse inválido)
        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), any(String.class)))
                .thenThrow(new ConstraintViolationException("Simulated validation error", null)); // Usar um conjunto vazio ou null para simplificar o mock

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar status 400 BAD REQUEST quando o serviço lança IllegalArgumentException")
    void shouldReturn400WhenServiceThrowsIllegalArgumentException() throws Exception {
        EmailDTO emailDTO = createValidEmailDTO();

        when(environment.getProperty("mail.integracao")).thenReturn("UNKNOWN"); // Simula integração desconhecida

        // Simula o serviço lançando uma exceção para integração desconhecida
        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("UNKNOWN")))
                .thenThrow(new IllegalArgumentException("Tipo de integração desconhecido: UNKNOWN"));

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isBadRequest());
    }
}