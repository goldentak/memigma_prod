package kz.memigma.project.services;

import kz.memigma.project.models.User;
import kz.memigma.project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindPeopleService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Map<String, Object> findSimilarUsers(String token, int page, int size) {
        String username = jwtUtil.extractUsername(token);
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Set<Long> currentLikes = new HashSet<>(Optional.ofNullable(currentUser.getLikedMemes()).orElse(List.of()));

        List<Map<String, Object>> similarities = userRepository.findAll().stream()
                .filter(user -> !user.getUsername().equals(username))
                .map(user -> {
                    Set<Long> userLikes = new HashSet<>(Optional.ofNullable(user.getLikedMemes()).orElse(List.of()));
                    Set<Long> intersection = new HashSet<>(currentLikes);
                    intersection.retainAll(userLikes);

                    int total = currentLikes.size() + userLikes.size();
                    int matches = intersection.size() * 2;

                    double percentage = total == 0 ? 0.0 : (matches * 100.0) / total;

                    Map<String, Object> result = new HashMap<>();
                    result.put("username", user.getUsername());
                    result.put("percentage", percentage);
                    return result;
                })
                .sorted((a, b) -> Double.compare((Double) b.get("percentage"), (Double) a.get("percentage")))
                .collect(Collectors.toList());

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, similarities.size());

        List<Map<String, Object>> paginated = fromIndex < toIndex ? similarities.subList(fromIndex, toIndex) : List.of();

        Map<String, Object> response = new HashMap<>();
        response.put("users", paginated);
        response.put("total", similarities.size());
        return response;
    }
}
