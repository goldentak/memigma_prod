package kz.memigma.project.payload;

import lombok.Data;

@Data
public class MemeActionRequest {
    private Long memeId;
    private String action;
}