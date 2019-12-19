package com.example.controller;

import com.example.services.EmailService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock("lock");

        System.out.println("Try to get lock");
        if (lock.tryLock()) {
            try {

                emailService.sendMail();
            } finally {
                lock.unlock();
            }
        }

        System.out.println("Doesn't get lock");
    }
}
