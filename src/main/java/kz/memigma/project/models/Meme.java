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
    private String creationDate;
    private String createdBy;
    //description
    private String language;
    private String type;
}
