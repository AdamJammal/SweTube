package io.bootify.swetube.domain;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long videoId;

    @Column(length = 255, nullable = false)
    private String videoTitle;

    @Column(length = 255)
    private String videoDescription;

    @Column(length = 255, nullable = false)
    private String videoUploadType;

    @Column(length = 255, nullable = false)
    private String videoUrl;

    @Column(length = 255)
    private String filePath;

    @Column(length = 255)
    private String videoThumbnailUrl;

    private LocalDate videoUploadDate;

    private Long videoViews = 0L;

    private Long videoLikes = 0L;

    private Long videoDislikes = 0L;

    @ManyToOne(optional = false) // Kopplar videon till en användare
    @JoinColumn(name = "user_id", nullable = false) // Specificerar foreign key
    private User user;
}
