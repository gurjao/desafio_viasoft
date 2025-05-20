package com.viasoft.email.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EmailAwsDTO {
    @NotBlank(message = "O email do destinatário é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 45, message = "O email do destinatário deve ter no máximo 45 caracteres")
    private String recipient;

    @NotBlank(message = "O nome do destinatário é obrigatório")
    @Size(max = 60, message = "O nome do destinatário deve ter no máximo 60 caracteres")
    private String recipientName;

    @NotBlank(message = "O email do remetente é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 45, message = "O email do remetente deve ter no máximo 45 caracteres")
    private String sender;

    @NotBlank(message = "O assunto do email é obrigatório")
    @Size(max = 120, message = "O assunto do email deve ter no máximo 120 caracteres")
    private String subject;

    @NotBlank(message = "O conteúdo do email é obrigatório")
    @Size(max = 256, message = "O conteúdo do email deve ter no máximo 256 caracteres")
    private String content;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}