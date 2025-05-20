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

public class EmailOciDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmailOciDTO createValidEmailOciDTO() {
        EmailOciDTO dto = new EmailOciDTO();
        dto.setRecipientEmail("oci.valid@example.com");
        dto.setRecipientEmail("OCI Valid Name");
        dto.setSenderEmail("oci.sender@example.com");
        dto.setSubject("OCI Subject");
        dto.setBody("OCI Content");
        return dto;
    }

    @Test
    @DisplayName("Deve validar um EmailOciDTO com dados válidos")
    void shouldValidateValidEmailOciDTO() {
        EmailOciDTO dto = createValidEmailOciDTO();
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um DTO OCI válido.");
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientEmail vazio")
    void shouldNotValidateEmailOciDTOWithEmptyRecipientEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientEmail("");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient Email (OCI) é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientEmail inválido")
    void shouldNotValidateEmailOciDTOWithInvalidRecipientEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientEmail("invalid-oci-email");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient Email (OCI) inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientEmail muito longo")
    void shouldNotValidateEmailOciDTOWithLongRecipientEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientEmail("oci.emailcompridodemaisparaoquarenta@longo.com"); // 46 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient Email (OCI) deve ter no máximo 40 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientName muito longo")
    void shouldNotValidateEmailOciDTOWithLongRecipientName() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientName("Nome muito muito muito muito muito muito muito longo OCI"); // 55 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Recipient Name (OCI) deve ter no máximo 50 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com senderEmail inválido")
    void shouldNotValidateEmailOciDTOWithInvalidSenderEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSenderEmail("invalid-oci-sender");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Sender Email (OCI) inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com subject muito longo")
    void shouldNotValidateEmailOciDTOWithLongSubject() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSubject("Este é um assunto super super super super super super super super super super super super longo para OCI."); // 106 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Subject (OCI) deve ter no máximo 100 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com body muito longo")
    void shouldNotValidateEmailOciDTOWithLongBody() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setBody("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Este é um conteúdo muito longo para OCI."); // 300+ caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Body (OCI) deve ter no máximo 250 caracteres.", violations.iterator().next().getMessage());
    }
}