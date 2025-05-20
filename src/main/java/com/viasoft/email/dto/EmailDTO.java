package com.viasoft.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailDTO {
    @NotBlank(message = "O email do destinatário é obrigatório")
    @Email(message = "Formato de email inválido")
    private String destinatarioEmail;

    @NotBlank(message = "O nome do destinatário é obrigatório")
    @Size(max = 255, message = "O nome do destinatário deve ter no máximo 255 caracteres")
    private String destinatarioNome;

    @NotBlank(message = "O email do remetente é obrigatório")
    @Email(message = "Formato de email inválido")
    private String remetenteEmail;

    @NotBlank(message = "O assunto do email é obrigatório")
    @Size(max = 255, message = "O assunto do email deve ter no máximo 255 caracteres")
    private String assunto;

    @NotBlank(message = "O conteúdo do email é obrigatório")
    @Size(max = 1000, message = "O conteúdo do email deve ter no máximo 1000 caracteres")
    private String conteudo;

    public String getDestinatarioEmail() {
        return destinatarioEmail;
    }

    public void setDestinatarioEmail(String destinatarioEmail) {
        this.destinatarioEmail = destinatarioEmail;
    }

    public String getDestinatarioNome() {
        return destinatarioNome;
    }

    public void setDestinatarioNome(String destinatarioNome) {
        this.destinatarioNome = destinatarioNome;
    }

    public String getRemetenteEmail() {
        return remetenteEmail;
    }

    public void setRemetenteEmail(String remetenteEmail) {
        this.remetenteEmail = remetenteEmail;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
