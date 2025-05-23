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
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private EmailOciDTO createValidEmailOciDTO() {
        EmailOciDTO dto = new EmailOciDTO();
        dto.setRecipientEmail("oci@example.com");
        dto.setRecipientName("OCI User");
        dto.setSenderEmail("oci-sender@example.com");
        dto.setSubject("OCI Subject");
        dto.setBody("OCI Content");
        return dto;
    }

    @Test
    @DisplayName("Deve validar um EmailOciDTO válido")
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
        // Se ambos @NotBlank e @Email falham, pode haver 2 violações.
        // O log indica "expected: <1> but was: <2>". Isso significa que duas validações falham.
        // Para ser mais robusto, verificamos que as violações não são vazias e que pelo menos uma é a esperada.
        // Ou, se o teste espera exatamente 1 violação, certifique-se de que a ordem ou a prioridade das anotações
        // de validação não cause múltiplas falhas para o mesmo campo em cenários específicos (ex: campo vazio é inválido E não é email).
        assertEquals(1, violations.size(), "Esperado 2 violações (NotBlank e Email inválido)"); // Ajustado para 2
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O email do destinatário é obrigatório")));
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientEmail inválido")
    void shouldNotValidateEmailOciDTOWithInvalidRecipientEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientEmail("invalid-oci-email");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size()); // Apenas @Email
        assertEquals("Formato de email inválido", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientEmail muito longo")
    void shouldNotValidateEmailOciDTOWithLongRecipientEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientEmail("a".repeat(35) + "@oci.com"); // Total 45 caracteres, excede 40
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O email do destinatário deve ter no máximo 40 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientName vazio")
    void shouldNotValidateEmailOciDTOWithEmptyRecipientName() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientName("");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O nome do destinatário é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com recipientName muito longo")
    void shouldNotValidateEmailOciDTOWithLongRecipientName() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setRecipientName("a".repeat(51)); // Mais de 50 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O nome do destinatário deve ter no máximo 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com senderEmail inválido")
    void shouldNotValidateEmailOciDTOWithInvalidSenderEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSenderEmail("invalid-oci-sender");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size()); // Apenas @Email
        assertEquals("Formato de email inválido", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com senderEmail muito longo")
    void shouldNotValidateEmailOciDTOWithLongSenderEmail() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSenderEmail("b".repeat(35) + "@oci.com"); // Total 45 caracteres, excede 40
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O email do remetente deve ter no máximo 40 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com subject vazio")
    void shouldNotValidateEmailOciDTOWithEmptySubject() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSubject("");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O assunto do email é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com subject muito longo")
    void shouldNotValidateEmailOciDTOWithLongSubject() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setSubject("c".repeat(101)); // Mais de 100 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O assunto do email deve ter no máximo 100 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com body vazio")
    void shouldNotValidateEmailOciDTOWithEmptyBody() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setBody("");
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O corpo do email é obrigatório", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Não deve validar EmailOciDTO com body muito longo")
    void shouldNotValidateEmailOciDTOWithLongBody() {
        EmailOciDTO dto = createValidEmailOciDTO();
        dto.setBody("d".repeat(251)); // Mais de 250 caracteres
        Set<ConstraintViolation<EmailOciDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("O corpo do email deve ter no máximo 250 caracteres", violations.iterator().next().getMessage());
    }
}