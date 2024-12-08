package io.bootify.swetube.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VideoWithUserDTO {

    private Long videoId;
    private String videoTitle;
    private String videoDescription;
    private String videoUploadType;
    private String videoUrl;
    private String userName;
    private LocalDate videoUploadDate;  // Lägg till detta fält
}
