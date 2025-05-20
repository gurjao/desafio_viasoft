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
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmailAwsDTO createValidEmailAwsDTO() {
        EmailAwsDTO dto = new EmailAwsDTO();
        dto.setRecipient("aws.valid@example.com");
        dto.setRecipientName("AWS Valid Name");
        dto.setSender("aws.sender@example.com");
        dto.setSubject("AWS Subject");
        dto.setContent("AWS Content");
        return dto;
    }

    @Test
    @DisplayName("Deve validar um EmailAwsDTO com dados válidos")
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
        assertEquals(1, violations.size());
        assertEquals("Recipient (AWS) é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipient inválido")
    void shouldNotValidateEmailAwsDTOWithInvalidRecipient() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipient("invalid-aws-email");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient (AWS) inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipient muito longo")
    void shouldNotValidateEmailAwsDTOWithLongRecipient() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipient("aws.emailcompridodemaisparaoquarentaecinco@longo.com"); // 50 caracteres
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient (AWS) deve ter no máximo 45 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com recipientName muito longo")
    void shouldNotValidateEmailAwsDTOWithLongRecipientName() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setRecipientName("Nome muito muito muito muito muito muito muito muito muito muito longo AWS"); // 70 caracteres
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient Name (AWS) deve ter no máximo 60 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com sender inválido")
    void shouldNotValidateEmailAwsDTOWithInvalidSender() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setSender("invalid-aws-sender");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Sender (AWS) inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com subject muito longo")
    void shouldNotValidateEmailAwsDTOWithLongSubject() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setSubject("Este é um assunto super super super super super super super super super super super super super super super super super super super super longo para AWS.");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Subject (AWS) deve ter no máximo 120 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailAwsDTO com content muito longo")
    void shouldNotValidateEmailAwsDTOWithLongContent() {
        EmailAwsDTO dto = createValidEmailAwsDTO();
        dto.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Este é um conteúdo muito longo para AWS.");
        Set<ConstraintViolation<EmailAwsDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Content (AWS) deve ter no máximo 256 caracteres.", violations.iterator().next().getMessage());
    }
}