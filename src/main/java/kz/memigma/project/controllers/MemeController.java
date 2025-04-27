package kz.memigma.project.controllers;

import jakarta.validation.Valid;
import kz.memigma.project.dto.MemeDto;
import kz.memigma.project.models.Meme;
import kz.memigma.project.services.MemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/memes")
@RequiredArgsConstructor
public class MemeController {

    private final MemeService memeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Meme> createMeme(
            @Valid @ModelAttribute MemeDto memeDto) {

        try {
            Meme createdMeme = memeService.createMeme(memeDto);
            return ResponseEntity
                    .created(URI.create("/api/memes/" + createdMeme.getId()))
                    .body(createdMeme);

        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error processing image", e
            );
        }
    }


}