package org.example;

import jakarta.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        EmailService emailService = new EmailService();
        emailService.sendMessage(emailService.getEmail());
    }
}
