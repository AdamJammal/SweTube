package io.bootify.swetube.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "video_like") // Ändra tabellnamnet
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video; // Relation till Video

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Relation till User

    private Boolean isLiked; // True för like, False för dislike
}
