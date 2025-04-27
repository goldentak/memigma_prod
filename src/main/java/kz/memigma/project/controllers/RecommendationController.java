package kz.memigma.project.controllers;


import kz.memigma.project.models.Meme;
import kz.memigma.project.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping
    public Page<Meme> recommend(
            @RequestParam(defaultValue = "date") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Principal principal
    ) {
        String username = principal.getName();
        return recommendationService.getRecommendations(username, filter, page, size);
    }
}

