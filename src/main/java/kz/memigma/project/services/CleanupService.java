package kz.memigma.project.services;

import kz.memigma.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CleanupService {
    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 60_000)
    public void evictUnconfirmed() {
        Instant cutoff = Instant.now().minus(5, ChronoUnit.MINUTES);
        userRepository.deleteAllByEnabledFalseAndCreatedAtBefore(cutoff);
    }
}
