package com.viasoft.email.service;

import com.viasoft.email.dto.EmailAwsDTO;
import com.viasoft.email.dto.EmailDTO;
import com.viasoft.email.dto.EmailOciDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private final Validator validator;

    @Autowired
    public EmailServiceImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Object adaptarEmailParaIntegracao(EmailDTO emailDTO, String integracao) {
        Object emailAdaptado;

        if ("AWS".equalsIgnoreCase(integracao)) {
            emailAdaptado = adaptarParaAws(emailDTO);
        } else if ("OCI".equalsIgnoreCase(integracao)) {
            emailAdaptado = adaptarParaOci(emailDTO);
        } else {
            // Se a integração não for reconhecida, você pode escolher lançar uma exceção
            // ou retornar o DTO original ou null, dependendo da sua regra de negócio.
            // Por simplicidade, vamos retornar o DTO original por enquanto, mas uma exceção
            // seria mais adequada para um tipo de integração inválido.
            throw new IllegalArgumentException("Tipo de integração desconhecido: " + integracao);
        }

        // Valida o DTO adaptado
        validateAdaptedDTO(emailAdaptado, integracao);

        return emailAdaptado;
    }

    @Override
    public EmailAwsDTO adaptarParaAws(EmailDTO emailDTO) {
        EmailAwsDTO awsDTO = new EmailAwsDTO();
        awsDTO.setRecipient(emailDTO.getDestinatarioEmail());
        awsDTO.setRecipientName(emailDTO.getDestinatarioNome());
        awsDTO.setSender(emailDTO.getRemetenteEmail());
        awsDTO.setSubject(emailDTO.getAssunto());
        awsDTO.setContent(emailDTO.getConteudo());
        return awsDTO;
    }

    @Override
    public EmailOciDTO adaptarParaOci(EmailDTO emailDTO) {
        EmailOciDTO ociDTO = new EmailOciDTO();
        ociDTO.setRecipientEmail(emailDTO.getDestinatarioEmail());
        ociDTO.setRecipientName(emailDTO.getDestinatarioNome());
        ociDTO.setSenderEmail(emailDTO.getRemetenteEmail());
        ociDTO.setSubject(emailDTO.getAssunto());
        ociDTO.setBody(emailDTO.getConteudo());
        return ociDTO;
    }

    private void validateAdaptedDTO(Object dto, String integracao) {
        Set<? extends ConstraintViolation<?>> violations;

        if ("AWS".equalsIgnoreCase(integracao)) {
            violations = validator.validate((EmailAwsDTO) dto);
        } else if ("OCI".equalsIgnoreCase(integracao)) {
            violations = validator.validate((EmailOciDTO) dto);
        } else {
            throw new IllegalArgumentException("Tipo de DTO inválido para validação: " + integracao);
        }

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}