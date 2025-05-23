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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmailService emailService;

    private static String tipoIntegracao = "AWS";

    @DynamicPropertySource
    static void dynamicProps(DynamicPropertyRegistry registry) {
        registry.add("mail.integracao", () -> tipoIntegracao);
    }

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
    @DisplayName("Deve retornar 204 NO CONTENT ao enviar email com sucesso (AWS)")
    void shouldReturn204WhenSendingEmailSuccessAws() throws Exception {
        tipoIntegracao = "AWS";
        EmailDTO emailDTO = createValidEmailDTO();
        EmailAwsDTO awsDTO = new EmailAwsDTO();
        awsDTO.setRecipient("valid@example.com");

        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("AWS")))
                .thenReturn(awsDTO);

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());

        verify(emailService).adaptarEmailParaIntegracao(any(EmailDTO.class), eq("AWS"));
    }

    @Test
    @DisplayName("Deve retornar 204 NO CONTENT ao enviar email com sucesso (OCI)")
    void shouldReturn204WhenSendingEmailSuccessOci() throws Exception {
        tipoIntegracao = "OCI";
        EmailDTO emailDTO = createValidEmailDTO();
        EmailOciDTO ociDTO = new EmailOciDTO();
        ociDTO.setRecipientEmail("valid@example.com");

        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("OCI")))
                .thenReturn(ociDTO);

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isNoContent());

        verify(emailService).adaptarEmailParaIntegracao(any(EmailDTO.class), eq("OCI"));
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST com DTO inválido")
    void shouldReturn400WhenInputDTOIsInvalid() throws Exception {
        EmailDTO dto = new EmailDTO();
        dto.setDestinatarioEmail("invalid-email");

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST quando o serviço lança ConstraintViolationException")
    void shouldReturn400WhenServiceThrowsConstraintViolationException() throws Exception {
        tipoIntegracao = "AWS";
        EmailDTO emailDTO = createValidEmailDTO();

        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("AWS")))
                .thenThrow(new ConstraintViolationException("Simulated validation error", Set.of()));

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 BAD REQUEST quando o serviço lança IllegalArgumentException")
    void shouldReturn400WhenServiceThrowsIllegalArgumentException() throws Exception {
        tipoIntegracao = "UNKNOWN";
        EmailDTO emailDTO = createValidEmailDTO();

        when(emailService.adaptarEmailParaIntegracao(any(EmailDTO.class), eq("UNKNOWN")))
                .thenThrow(new IllegalArgumentException("Tipo de integração desconhecido: UNKNOWN"));

        mockMvc.perform(post("/api/email/enviar-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDTO)))
                .andExpect(status().isBadRequest());
    }
}