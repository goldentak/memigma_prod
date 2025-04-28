package kz.memigma.project.controllers;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sidebar")
@RequiredArgsConstructor
public class SideBarController {

    private final UserRepository userRepository;
    private final MemeRepository memeRepository;

    @GetMapping("/likes")
    public List<Meme> recentLikes(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> likedIds = user.getLikedMemes();
        return likedIds.isEmpty() ? List.of() :
                memeRepository.findTop5ByIdInOrderByCreationDateDesc(likedIds);
    }

    @GetMapping("/views")
    public List<Meme> recentViews(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Long> viewedIds = user.getViewedMemes();
        return viewedIds.isEmpty() ? List.of() :
                memeRepository.findTop5ByIdInOrderByCreationDateDesc(viewedIds);
    }
}

