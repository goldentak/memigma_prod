package kz.memigma.project.services;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import kz.memigma.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository     userRepository;
    private final MemeRepository     memeRepository;
    private final PasswordEncoder    passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       MemeRepository memeRepository) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.memeRepository = memeRepository;
    }

    public User register(User user) {
        user.setEnabled(true);
        user.setCreatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public boolean login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(rawPassword, u.getPassword()))
                .isPresent();
    }

    public List<Meme> getRecommendations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Long> viewedMemes = user.getViewedMemes();

        if (viewedMemes.isEmpty()) {
            return memeRepository.findAll().stream()
                    .limit(10)
                    .toList();
        }

        return memeRepository.findByIdNotIn(viewedMemes,
                        org.springframework.data.domain.PageRequest.of(0, 10))
                .getContent();
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }
}
