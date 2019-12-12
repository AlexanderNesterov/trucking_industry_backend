package com.example.controller;

import com.example.services.EmailService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SendingEmailController {

    private final EmailService emailService;

    public SendingEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(fixedRate = 50_000)
    public void sendMail() {
        emailService.sendMail();
    }
}
