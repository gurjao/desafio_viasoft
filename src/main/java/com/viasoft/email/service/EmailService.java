package com.viasoft.email.service;

import com.viasoft.email.dto.EmailAwsDTO;
import com.viasoft.email.dto.EmailDTO;
import com.viasoft.email.dto.EmailOciDTO;

public interface EmailService {
    // Metodo que adapta o DTO de entrada para o DTO específico da integração
    Object adaptarEmailParaIntegracao(EmailDTO emailDTO, String integracao);

    // Métodos privados (ou protegidos, se for estendido) ou de classe que podem ser testados individualmente
    EmailAwsDTO adaptarParaAws(EmailDTO emailDTO);
    EmailOciDTO adaptarParaOci(EmailDTO emailDTO);
}