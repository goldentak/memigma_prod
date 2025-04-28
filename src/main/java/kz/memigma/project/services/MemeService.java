package kz.memigma.project.services;

import jakarta.transaction.Transactional;
import kz.memigma.project.dto.MemeDto;
import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemeService {

    private final MemeRepository memeRepository;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @Transactional
    public Meme createMeme(MemeDto dto) throws IOException {
        String imageUrl = s3Service.uploadImage(dto.getImage());

        Meme meme = new Meme();
        meme.setImgUrl(imageUrl);
        meme.setCreationDate(LocalDateTime.now());
        meme.setCreatedBy(dto.getUsername());
        meme.setTitle(dto.getTitle());

        return memeRepository.save(meme);
    }

    public List<Meme> getRecommendations(String username) {
        return memeRepository.findAll(PageRequest.of(0, 10)).getContent();
    }

    //recent
    public List<Meme> getRecentRecommendations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> viewed = user.getViewedMemes();

        if (viewed.isEmpty()) {
            return memeRepository.findAll(
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate"))
            ).getContent();
        } else {
            return memeRepository.findByIdNotIn(
                    viewed,
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "creationDate"))
            ).getContent();
        }
    }

    //popular
    public List<Meme> getPopularRecommendations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Long> viewed = user.getViewedMemes();

        if (viewed.isEmpty()) {
            return memeRepository.findAll(
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "likes"))
            ).getContent();
        } else {
            return memeRepository.findByIdNotIn(
                    viewed,
                    PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "likes"))
            ).getContent();
        }
    }
}