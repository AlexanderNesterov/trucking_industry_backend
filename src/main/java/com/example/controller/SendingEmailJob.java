package com.example.controller;

import com.example.services.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SendingEmailJob {

    private final EmailService emailService;

    public SendingEmailJob(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 50_000)
    public void sendMail() {
        emailService.sendMail();
    }
}
