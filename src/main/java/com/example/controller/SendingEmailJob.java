package com.example.controller;

import com.example.services.EmailService;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
public class SendingEmailJob {

    private final EmailService emailService;
    private final HazelcastInstance hazelcastInstance;

    public SendingEmailJob(EmailService emailService,
                           @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.emailService = emailService;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Scheduled(fixedRate = 50_000)
    public void sendMail() {
        Lock lock = hazelcastInstance.getLock("lock");
        lock.lock();

        try {
            emailService.sendMail();
        } finally {
            lock.unlock();
        }
    }
}
