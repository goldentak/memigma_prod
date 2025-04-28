package kz.memigma.project.controllers;


import kz.memigma.project.models.Meme;
import kz.memigma.project.services.MemeService;
import kz.memigma.project.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecommendationController {
    private final MemeService memeService;
    private final RecommendationService recommendationService;

    @GetMapping("/recommend")
    public ResponseEntity<Page<Meme>> recommend(
            Authentication auth,
            @RequestParam(defaultValue = "date") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        String username = auth.getName();
        Page<Meme> recs = recommendationService.getRecommendations(username, filter, page, size);
        return ResponseEntity.ok(recs);
    }

    @GetMapping("/recommendations")
    public ResponseEntity<?> recommend(
            Principal principal,
            @RequestParam(value = "filter", defaultValue = "date") String filter) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        String username = principal.getName();
        List<Meme> recommendations;

        switch (filter.toLowerCase()) {
            case "date":
                recommendations = memeService.getRecentRecommendations(username);
                break;
            case "popular":
                recommendations = memeService.getPopularRecommendations(username);
                break;
            default:
                recommendations = memeService.getRecommendations(username);
                break;
        }

        return ResponseEntity.ok(Collections.singletonMap("content", recommendations));
    }
}

