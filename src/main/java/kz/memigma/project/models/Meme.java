package kz.memigma.project.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "memes")
@Data
public class Meme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String imgUrl;

    //description
    private String language;
    private String type;
}
