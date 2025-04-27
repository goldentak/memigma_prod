package kz.memigma.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemeDto {

    @NotNull(message = "Image is required")
    private MultipartFile image;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Title is required")
    private String title;
}
