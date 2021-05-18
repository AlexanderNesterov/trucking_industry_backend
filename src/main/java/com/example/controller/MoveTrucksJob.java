package com.example.controller;

import com.example.services.MovementSimulationService;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MoveTrucksJob {

    private final MovementSimulationService movementSimulationService;
    private final HazelcastInstance hazelcastInstance;

    public MoveTrucksJob(MovementSimulationService movementSimulationService,
                           @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {
        this.movementSimulationService = movementSimulationService;
        this.hazelcastInstance = hazelcastInstance;
    }

    @Scheduled(fixedRate = 1_000)
    public void moveTrucks() {
        FencedLock lock = hazelcastInstance.getCPSubsystem().getLock("lock");

        System.out.println("Try to get lock");
        if (lock.tryLock()) {
            try {

                movementSimulationService.moveTrucks();
            } finally {
                lock.unlock();
            }
        }

        System.out.println("Doesn't get lock");
    }
}
