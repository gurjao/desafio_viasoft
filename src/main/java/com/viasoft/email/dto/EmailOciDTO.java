package com.viasoft.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailOciDTO {
    @NotBlank(message = "O email do destinatário é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 40, message = "O email do destinatário deve ter no máximo 40 caracteres")
    private String recipientEmail;

    @NotBlank(message = "O nome do destinatário é obrigatório")
    @Size(max = 50, message = "O nome do destinatário deve ter no máximo 50 caracteres")
    private String recipientName;

    @NotBlank(message = "O email do remetente é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 40, message = "O email do remetente deve ter no máximo 40 caracteres")
    private String senderEmail;

    @NotBlank(message = "O assunto do email é obrigatório")
    @Size(max = 100, message = "O assunto do email deve ter no máximo 100 caracteres")
    private String subject;

    @NotBlank(message = "O corpo do email é obrigatório")
    @Size(max = 250, message = "O corpo do email deve ter no máximo 250 caracteres")
    private String body;

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}