package kz.memigma.project.services;

import jakarta.transaction.Transactional;
import kz.memigma.project.dto.MemeDto;
import kz.memigma.project.models.Meme;
import kz.memigma.project.repositories.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemeService {

    private final MemeRepository memeRepository;
    private final S3Service s3Service;

    @Transactional
    public Meme createMeme(MemeDto dto) throws IOException {
        String imageUrl = s3Service.uploadImage(dto.getImage());

        Meme meme = new Meme();
        meme.setImgUrl(imageUrl);
        meme.setCreationDate(LocalDateTime.now());
        meme.setCreatedBy(dto.getUsername());

        return memeRepository.save(meme);
    }
}