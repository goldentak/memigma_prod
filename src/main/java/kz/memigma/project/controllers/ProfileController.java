package kz.memigma.project.controllers;

import kz.memigma.project.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ProfileController {

    private final MemeRepository memeRepository;
    private final UserService userService;
    private final ObjectMapper mapper = new ObjectMapper();

    public ProfileController(MemeRepository memeRepository,
                             UserService userService) {
        this.memeRepository = memeRepository;
        this.userService     = userService;
    }

    @GetMapping("/api/user-profile")
    public Map<String, Object> getUserProfile(
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String token = authHeader.substring(7);
        try {
            String[] parts = token.split("\\.");
            if (parts.length < 2) {
                throw new IllegalArgumentException("Bad JWT format");
            }
            byte[]   decoded = Base64.getUrlDecoder().decode(parts[1]);
            JsonNode payload = mapper.readTree(new String(decoded, StandardCharsets.UTF_8));
            String   username = payload.path("sub").asText(null);
            if (username == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token payload");
            }

            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

            Map<String, Object> profile = new HashMap<>();
            profile.put("username", user.getUsername());
            profile.put("email",    user.getEmail());
            return profile;

        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    @GetMapping("/api/liked-memes")
    public Map<String, Object> getLikedMemes(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        List<Map<String, String>> memes = memeRepository.findAll().stream()
                .limit(12)
                .map(meme -> Map.of("url", meme.getImgUrl()))
                .collect(Collectors.toList());

        return Map.of("memes", memes);
    }
}

