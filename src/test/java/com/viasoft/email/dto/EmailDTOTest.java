package com.viasoft.email.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private EmailDTO createValidEmailDTO() {
        EmailDTO dto = new EmailDTO();
        dto.setDestinatarioEmail("valid@example.com");
        dto.setDestinatarioNome("Valid User Name");
        dto.setRemetenteEmail("sender@example.com");
        dto.setAssunto("Test Subject");
        dto.setConteudo("This is the content of the email.");
        return dto;
    }

    @Test
    @DisplayName("Deve validar um EmailDTO com dados válidos")
    void shouldValidateValidEmailDTO() {
        EmailDTO dto = createValidEmailDTO();
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Não deveria haver violações para um DTO válido.");
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com destinatarioEmail vazio")
    void shouldNotValidateEmailDTOWithEmptyDestinatarioEmail() {
        EmailDTO dto = createValidEmailDTO();
        dto.setDestinatarioEmail("");
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("E-mail do destinatário é obrigatório.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com destinatarioEmail inválido")
    void shouldNotValidateEmailDTOWithInvalidDestinatarioEmail() {
        EmailDTO dto = createValidEmailDTO();
        dto.setDestinatarioEmail("invalid-email");
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("E-mail do destinatário inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com destinatarioEmail muito longo")
    void shouldNotValidateEmailDTOWithLongDestinatarioEmail() {
        EmailDTO dto = createValidEmailDTO();
        dto.setDestinatarioEmail("emailcompridodemaisparaoquarentaecinco@muitolongodemail.com"); // 54 caracteres
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("E-mail do destinatário deve ter no máximo 45 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com destinatarioNome muito longo")
    void shouldNotValidateEmailDTOWithLongDestinatarioNome() {
        EmailDTO dto = createValidEmailDTO();
        dto.setDestinatarioNome("Nome muito muito muito muito muito muito muito muito muito muito muito longo"); // 71 caracteres
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Nome do destinatário deve ter no máximo 60 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com remetenteEmail inválido")
    void shouldNotValidateEmailDTOWithInvalidRemetenteEmail() {
        EmailDTO dto = createValidEmailDTO();
        dto.setRemetenteEmail("invalid-sender");
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("E-mail do remetente inválido.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com assunto muito longo")
    void shouldNotValidateEmailDTOWithLongAssunto() {
        EmailDTO dto = createValidEmailDTO();
        dto.setAssunto("Este é um assunto super super super super super super super super super super super super super super super longo que excede o limite de caracteres permitido."); // 134 caracteres
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Assunto deve ter no máximo 120 caracteres.", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailDTO com conteúdo muito longo")
    void shouldNotValidateEmailDTOWithLongConteudo() {
        EmailDTO dto = createValidEmailDTO();
        dto.setConteudo("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Este é um conteúdo muito longo para o limite de 256 caracteres."); // 500+ caracteres
        Set<ConstraintViolation<EmailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("Conteúdo deve ter no máximo 256 caracteres.", violations.iterator().next().getMessage());
    }
}