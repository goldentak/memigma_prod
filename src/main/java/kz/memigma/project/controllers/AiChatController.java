package kz.memigma.project.controllers;

import kz.memigma.project.services.JwtUtil;
import kz.memigma.project.services.MemeRecommendationAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AiChatController {
    private final JwtUtil jwtUtil;
    private final MemeRecommendationAiService aiService;

    @PostMapping("/ai-chat")
    public Map<String, String> getAiRecommendations(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> body
    ) {
        String prompt = body.getOrDefault("prompt", "");
        String token = authorizationHeader.replace("Bearer ", "").trim();
        String username = jwtUtil.extractUsername(token);
        String aiResponse = aiService.getRecommendationsFromOpenAi(username, prompt);

        return Map.of("response", aiResponse);
    }
}
