package kz.memigma.project.controllers;

import kz.memigma.project.models.User;
import kz.memigma.project.payload.MemeActionRequest;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemeActionController {

    private final UserRepository userRepository;

    @PostMapping("/meme-action")
    public ResponseEntity<?> memeAction(@RequestBody MemeActionRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long memeId = request.getMemeId();
        String action = request.getAction();

        if (memeId == null || action == null) {
            return ResponseEntity.badRequest().body("Invalid request data");
        }

        switch (action.toLowerCase()) {
            case "like" -> {
                if (!user.getLikedMemes().contains(memeId)) {
                    user.getLikedMemes().add(memeId);
                }
                if (!user.getViewedMemes().contains(memeId)) {
                    user.getViewedMemes().add(memeId);
                }
            }
            case "dislike" -> {
                if (!user.getViewedMemes().contains(memeId)) {
                    user.getViewedMemes().add(memeId);
                }
            }
            case "favorite" -> {
                if (!user.getFavoriteMemes().contains(memeId)) {
                    user.getFavoriteMemes().add(memeId);
                }
                if (!user.getViewedMemes().contains(memeId)) {
                    user.getViewedMemes().add(memeId);
                }
            }
            default -> {
                return ResponseEntity.badRequest().body("Unknown action: " + action);
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}

