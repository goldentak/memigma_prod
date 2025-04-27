package kz.memigma.project.services;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final MemeRepository memeRepository;

    public Page<Meme> getRecommendations(String username, String filter, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Long> viewed = user.getViewedMemes();
        Sort sort = switch (filter) {
            case "popularity" -> Sort.by(Sort.Direction.DESC, "likes");
            case "date" -> Sort.by(Sort.Direction.DESC, "creationDate");
            default -> Sort.by(Sort.Direction.DESC, "creationDate");
        };

        var pageable = PageRequest.of(page, size, sort);
        if (viewed.isEmpty()) {
            return memeRepository.findAll(pageable);
        } else {
            return memeRepository.findByIdNotIn(viewed, pageable);
        }
    }
}


