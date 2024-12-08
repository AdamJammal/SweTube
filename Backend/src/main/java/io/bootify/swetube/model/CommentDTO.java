package io.bootify.swetube.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDTO {
    private Long id;
    private Long userId;
    private Long videoId;
    private String text;
    private LocalDateTime createdAt;
    private String userName; // Användarnamn för användaren
    private String avatarUrl; // URL till användarens avatar
}
