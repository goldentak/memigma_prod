package kz.memigma.project.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PendingRegistrationService {
    public static class Pending{
        public final String email, username, password, code;
        public final Instant createdAt;
        Pending(String email, String username, String password, String code) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.code = code;
            this.createdAt = Instant.now();
        }
    }
    private final Map<String, Pending> pendings = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public String start(String email, String username, String password) {
        String code = String.format("%06d", random.nextInt(1_000_000));
        pendings.put(username, new Pending(email, username, password, code));
        return code;
    }

    public Pending verify(String username, String code) {
        Pending p = pendings.get(username);
        if (p != null
                && p.code.equals(code)
                && Instant.now().isBefore(p.createdAt.plusSeconds(300)))  // 300s = 5min
        {
            pendings.remove(username);
            return p;
        }
        return null;
    }

    @Scheduled(fixedRate = 60_000)
    private void evictExpired() {
        Instant cutoff = Instant.now().minusSeconds(300);
        pendings.entrySet().removeIf(e -> e.getValue().createdAt.isBefore(cutoff));
    }

    public Pending getPending(String username) {
        return pendings.get(username);
    }

    public String refreshCode(String username) {
        Pending p = pendings.get(username);
        if (p != null) {
            String newCode = String.format("%06d", random.nextInt(1_000_000));
            pendings.put(username, new Pending(p.email, p.username, p.password, newCode));
            return newCode;
        }
        throw new RuntimeException("Pending registration not found");
    }
}
