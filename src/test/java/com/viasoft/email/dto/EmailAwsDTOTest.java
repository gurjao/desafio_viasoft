package com.viasoft.email.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailAwsDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private EmailAwsDTO createValidEmailAwsDTO() {
        EmailAwsDTO dto = new EmailAwsDTO();
        dto.setRecipient("aws@example.com");
        dto.setRecipientName("AWS User");
        dto.setSender("aws-sender@example.com");
        dto.setSubject("AWS Subject");
        dto.setContent("AWS Content");
        return dto;
    }

    @Test
    @DisplayName("Deve validar um EmailAwsDTO válido")
    void shouldValidateValidEmailAwsDTO() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um DTO AWS válido.");
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipient vazio")
    void shouldNotValidateEmailAwsDTOWithEmptyRecipient() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipient("");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size()); // Apenas @NotBlank
        assertEquals("O email do destinatário é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipient inválido")
    void shouldNotValidateEmailAwsDTOWithInvalidRecipient() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipient("invalid-aws-recipient");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size()); // Apenas @Email
        assertEquals("Formato de email inválido", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipient muito longo")
    void shouldNotValidateEmailAwsDTOWithLongRecipient() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipient("a".repeat(40) + "@example.com"); // Total 51 caracteres, excede 45
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O email do destinatário deve ter no máximo 45 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipientName vazio")
    void shouldNotValidateEmailAwsDTOWithEmptyRecipientName() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipientName("");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O nome do destinatário é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipientName muito longo")
    void shouldNotValidateEmailAwsDTOWithLongRecipientName() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipientName("a".repeat(61)); // Mais de 60 caracteres
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O nome do destinatário deve ter no máximo 60 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com sender inválido")
    void shouldNotValidateEmailAwsDTOWithInvalidSender() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setSender("invalid-aws-sender");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Formato de email inválido", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com sender muito longo")
    void shouldNotValidateEmailAwsDTOWithLongSender() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setSender("b".repeat(40) + "@example.com"); // Total 51 caracteres, excede 45
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O email do remetente deve ter no máximo 45 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com subject muito longo")
    void shouldNotValidateEmailAwsDTOWithLongSubject() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setSubject("c".repeat(121)); // Mais de 120 caracteres
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O assunto do email deve ter no máximo 120 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com content muito longo")
    void shouldNotValidateEmailAwsDTOWithLongContent() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setContent("d".repeat(257)); // Mais de 256 caracteres
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O conteúdo do email deve ter no máximo 256 caracteres", violations.iterator().next().getMessage());
    }
}