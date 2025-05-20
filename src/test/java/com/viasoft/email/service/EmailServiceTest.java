package com.viasoft.email.service;

import com.viasoft.email.dto.EmailAwsDTO;
import com.viasoft.email.dto.EmailDTO;
import com.viasoft.email.dto.EmailOciDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class EmailServiceTest {

    private EmailService emailService;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        emailService = new EmailServiceImpl(validator); // Instancia o serviço com o validator
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
    @DisplayName("Deve adaptar EmailDTO para EmailAwsDTO corretamente")
    void shouldAdaptEmailDTOToEmailAwsDTO() {
        EmailDTO emailDTO = createValidEmailDTO();
        EmailAwsDTO awsDTO = emailService.adaptarParaAws(emailDTO); // Chamando o método adaptado

        assertNotNull(awsDTO);
        assertEquals(emailDTO.getDestinatarioEmail(), awsDTO.getRecipient());
        assertEquals(emailDTO.getDestinatarioNome(), awsDTO.getRecipientName());
        assertEquals(emailDTO.getRemetenteEmail(), awsDTO.getSender());
        assertEquals(emailDTO.getAssunto(), awsDTO.getSubject());
        assertEquals(emailDTO.getConteudo(), awsDTO.getContent());
    }

    @Test
    @DisplayName("Deve adaptar EmailDTO para EmailOciDTO corretamente")
    void shouldAdaptEmailDTOToEmailOciDTO() {
        EmailDTO emailDTO = createValidEmailDTO();
        EmailOciDTO ociDTO = emailService.adaptarParaOci(emailDTO); // Chamando o método adaptado

        assertNotNull(ociDTO);
        assertEquals(emailDTO.getDestinatarioEmail(), ociDTO.getRecipientEmail());
        assertEquals(emailDTO.getDestinatarioNome(), ociDTO.getRecipientName());
        assertEquals(emailDTO.getRemetenteEmail(), ociDTO.getSenderEmail());
        assertEquals(emailDTO.getAssunto(), ociDTO.getSubject());
        assertEquals(emailDTO.getConteudo(), ociDTO.getBody());
    }

    @Test
    @DisplayName("Deve adaptar EmailDTO para AWS e validar com sucesso")
    void shouldAdaptAndValidateForAwsSuccess() {
        EmailDTO emailDTO = createValidEmailDTO();
        Object adaptedDTO = emailService.adaptarEmailParaIntegracao(emailDTO, "AWS");
        assertNotNull(adaptedDTO);
        assertTrue(adaptedDTO instanceof EmailAwsDTO);
    }

    @Test
    @DisplayName("Deve adaptar EmailDTO para OCI e validar com sucesso")
    void shouldAdaptAndValidateForOciSuccess() {
        EmailDTO emailDTO = createValidEmailDTO();
        Object adaptedDTO = emailService.adaptarEmailParaIntegracao(emailDTO, "OCI");
        assertNotNull(adaptedDTO);
        assertTrue(adaptedDTO instanceof EmailOciDTO);
    }

    @Test
    @DisplayName("Deve lançar ConstraintViolationException ao adaptar para AWS com DTO inválido")
    void shouldThrowExceptionWhenAdaptingToAwsWithInvalidDTO() {
        EmailDTO emailDTO = createValidEmailDTO();
        emailDTO.setDestinatarioEmail("invalid-email"); // Torna o DTO de entrada inválido
        // Ou, para testar a validação do AWS DTO:
        emailDTO.setDestinatarioEmail("valid@example.com"); // Válido para o input
        emailDTO.setRemetenteEmail("invalid-sender"); // Isso causará um erro no EmailAwsDTO.sender

        // O serviço lançará a exceção de validação do DTO adaptado
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            emailService.adaptarEmailParaIntegracao(emailDTO, "AWS");
        });

        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("Sender (AWS) inválido.", exception.getConstraintViolations().iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException para integração desconhecida")
    void shouldThrowIllegalArgumentExceptionForUnknownIntegration() {
        EmailDTO emailDTO = createValidEmailDTO();
        assertThrows(IllegalArgumentException.class, () -> {
            emailService.adaptarEmailParaIntegracao(emailDTO, "UNKNOWN");
        }, "Tipo de integração desconhecido: UNKNOWN");
    }
}