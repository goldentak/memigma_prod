package kz.memigma.project.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class MemeDto {
    private MultipartFile image;

    private String language;

    private List<String> types;
}