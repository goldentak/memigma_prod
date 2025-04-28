package kz.memigma.project.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean enabled = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @ElementCollection
    @CollectionTable(
            name = "user_viewed_memes",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "meme_id", nullable = false)
    private List<Long> viewedMemes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_liked_memes",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "meme_id", nullable = false)
    private List<Long> likedMemes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "user_favorite_memes",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "meme_id", nullable = false)
    private List<Long> favoriteMemes = new ArrayList<>();


}