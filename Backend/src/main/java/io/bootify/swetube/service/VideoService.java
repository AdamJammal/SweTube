package io.bootify.swetube.service;

import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import io.bootify.swetube.model.VideoDTO;
import io.bootify.swetube.model.VideoWithUserDTO;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.repos.VideoRepository;
import io.bootify.swetube.util.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final UserService userService;  // Injicera UserService

    @Autowired
    public VideoService(VideoRepository videoRepository, UserRepository userRepository, UserService userService) {
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
        this.userService = userService;  // Initialisera UserService
    }

    public List<VideoWithUserDTO> findAllWithUser() {
        // Hämta alla videor med användarinformation (via EntityGraph)
        List<Video> videos = videoRepository.findAll();

        return videos.stream()
                .map(video -> new VideoWithUserDTO(
                        video.getVideoId(),
                        video.getVideoTitle(),
                        video.getVideoDescription(),
                        video.getVideoUploadType(),
                        video.getVideoUrl(),
                        video.getUser() != null ? video.getUser().getUserName() : "Unknown User",
                        video.getVideoUploadDate()
                ))
                .toList();
    }

    public VideoDTO get(Long id) {
        return videoRepository.findById(id)
                .map(video -> mapToDTO(video, new VideoDTO()))
                .orElseThrow(() -> new NotFoundException("Video not found with ID: " + id));
    }

    private VideoDTO mapToDTO(Video video, VideoDTO videoDTO) {
        videoDTO.setVideoId(video.getVideoId());
        videoDTO.setVideoTitle(video.getVideoTitle());
        videoDTO.setVideoUrl(video.getVideoUrl());
        videoDTO.setVideoUploadedBy(video.getUser() != null ? video.getUser().getUserId() : null);
        videoDTO.setVideoDescription(video.getVideoDescription());
        videoDTO.setVideoUploadDate(video.getVideoUploadDate());
        return videoDTO;
    }


    public Long create(VideoDTO videoDTO) {
        // Hämta användaren från userId i videoDTO
        User user = userRepository.findById(videoDTO.getVideoUploadedBy())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Skapa Video entitet
        Video video = new Video();
        video.setVideoTitle(videoDTO.getVideoTitle());
        video.setVideoUploadType(videoDTO.getVideoUploadType());  // Här sätter vi 'YouTube' direkt från DTO
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setVideoDescription(videoDTO.getVideoDescription());
        video.setVideoUploadDate(videoDTO.getVideoUploadDate());
        video.setUser(user);  // Sätt användaren på videon

        // Spara Video i databasen
        return videoRepository.save(video).getVideoId();
    }

    private Video mapToEntity(VideoDTO videoDTO, Video video) {
        video.setVideoTitle(videoDTO.getVideoTitle());
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setVideoDescription(videoDTO.getVideoDescription());
        video.setVideoUploadDate(videoDTO.getVideoUploadDate());

        // Sätt användaren på video
        if (videoDTO.getVideoUploadedBy() != null) {
            User user = userService.findById(videoDTO.getVideoUploadedBy());  // Hämta användaren från userId
            if (user != null) {
                video.setUser(user);  // Sätt användaren på videon
            } else {
                throw new NotFoundException("User not found with ID: " + videoDTO.getVideoUploadedBy());
            }
        }

        return video;
    }


}
