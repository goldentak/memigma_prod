package kz.memigma.project.services;

import kz.memigma.project.models.Meme;
import kz.memigma.project.models.User;
import kz.memigma.project.repositories.MemeRepository;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemeRecommendationAiService {

    private final UserRepository userRepository;
    private final MemeRepository memeRepository;
    private final GeminiService geminiService;

    private static final String BASE_SYSTEM_PROMPT =
            "You are a friendly meme recommendation assistant. " +
                    "Start with a short greeting like 'Hey! Let me suggest you some memes based on your vibe:'. " +
                    "Then suggest 3-5 new meme ideas or famous meme formats without explaining your choices. " +
                    "List them clearly and briefly. " +
                    "End with a polite question like 'Need more ideas? Just ask!'. " +
                    "Keep the tone casual, natural, and human-like. " +
                    "Do not mention the user's previous favorites. " +
                    "Always respond in the same language the user uses.";

    public String generatePrompt(String username, String userPrompt) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return BASE_SYSTEM_PROMPT + " User message: \"" + userPrompt + "\"";
        }

        User user = userOpt.get();
        List<Long> likedMemes = user.getLikedMemes();

        if (likedMemes == null || likedMemes.isEmpty()) {
            return BASE_SYSTEM_PROMPT + " User message: \"" + userPrompt + "\"";
        }

        List<Meme> memes = memeRepository.findAllById(likedMemes);

        String titles = memes.stream()
                .map(Meme::getTitle)
                .filter(title -> title != null && !title.isBlank())
                .collect(Collectors.joining(" - "));

        if (titles.isBlank()) {
            return BASE_SYSTEM_PROMPT + " User message: \"" + userPrompt + "\"";
        }

        return BASE_SYSTEM_PROMPT +
                " Based on these meme titles: [" + titles + "] and user's message: \"" + userPrompt + "\"";
    }

    public String getRecommendationsFromOpenAi(String username, String userPrompt) {
        String fullPrompt = generatePrompt(username, userPrompt);
        return geminiService.sendMessage(fullPrompt);
    }
}
