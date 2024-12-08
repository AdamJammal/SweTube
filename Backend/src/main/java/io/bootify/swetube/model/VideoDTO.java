package io.bootify.swetube.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VideoDTO {

    private Long videoId; // För att representera ID vid behov

    private String videoTitle;

    private String videoDescription;

    private String videoUploadType; // "YouTube" eller "File"

    private String videoUrl; // URL till YouTube eller filväg

    private String videoThumbnailUrl; // Thumbnail-URL (om tillgängligt)

    private Long videoUploadedBy;

    private LocalDate videoUploadDate;

    private Long videoViews;

    private Long videoLikes;

    private Long videoDislikes;
}