package io.bootify.swetube.util;

import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import io.bootify.swetube.model.VideoDTO;
import io.bootify.swetube.repos.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class VideoMapper {

    private final UserRepository userRepository;

    public VideoMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public VideoDTO toDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setVideoId(video.getVideoId());
        videoDTO.setVideoTitle(video.getVideoTitle());
        videoDTO.setVideoDescription(video.getVideoDescription());
        videoDTO.setVideoUploadType(video.getVideoUploadType());
        videoDTO.setVideoUrl(video.getVideoUrl());
        videoDTO.setVideoThumbnailUrl(video.getVideoThumbnailUrl());
        videoDTO.setVideoUploadDate(video.getVideoUploadDate());
        videoDTO.setVideoViews(video.getVideoViews());
        videoDTO.setVideoLikes(video.getVideoLikes());
        videoDTO.setVideoDislikes(video.getVideoDislikes());

        // Sätt användar-ID från User
        if (video.getUser() != null) {
            videoDTO.setVideoUploadedBy(video.getUser().getUserId());
        }

        return videoDTO;
    }

    public Video toEntity(VideoDTO videoDTO) {
        Video video = new Video();
        video.setVideoTitle(videoDTO.getVideoTitle());
        video.setVideoDescription(videoDTO.getVideoDescription());
        video.setVideoUploadType(videoDTO.getVideoUploadType());
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setVideoUploadDate(videoDTO.getVideoUploadDate() != null ? videoDTO.getVideoUploadDate() : LocalDate.now());

        // Hämta användaren baserat på userId
        if (videoDTO.getVideoUploadedBy() != null) {
            User user = userRepository.findById(videoDTO.getVideoUploadedBy())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + videoDTO.getVideoUploadedBy()));
            video.setUser(user);
        }
        return video;
    }
}
