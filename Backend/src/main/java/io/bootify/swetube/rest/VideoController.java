package io.bootify.swetube.rest;

import io.bootify.swetube.domain.ApiResponse;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import io.bootify.swetube.model.VideoDTO;
import io.bootify.swetube.model.VideoWithUserDTO;
import io.bootify.swetube.service.LikeService;
import io.bootify.swetube.service.UserService;
import io.bootify.swetube.service.VideoService;
import io.bootify.swetube.util.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;
    private final LikeService likeService;
    private final UserService userService;  // Lägg till UserService här

    @Autowired
    public VideoController(VideoService videoService, LikeService likeService, UserService userService) {
        this.videoService = videoService;
        this.likeService = likeService;
        this.userService = userService;  // Injicera UserService
    }

    @PostMapping
    public ResponseEntity<?> addVideo(@Valid @RequestBody VideoDTO videoDTO, @RequestParam Long userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("User ID cannot be null", false));
        }

        // Hämta användaren baserat på userId
        User user = userService.findById(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User not found", false));
        }

        // Skapa Video entitet och sätt användare
        Video video = new Video();
        video.setVideoTitle(videoDTO.getVideoTitle());
        video.setVideoDescription(videoDTO.getVideoDescription());
        video.setVideoUploadType(videoDTO.getVideoUploadType());
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setVideoThumbnailUrl(videoDTO.getVideoThumbnailUrl());
        video.setVideoUploadDate(videoDTO.getVideoUploadDate());
        video.setUser(user); // Sätt användaren här

        // Skapa VideoDTO och sätt data
        videoDTO.setVideoUploadedBy(user.getUserId());

        // Spara video i databasen via videoService
        videoService.create(videoDTO);  // Nu skicka VideoDTO istället för Video-entitet

        return ResponseEntity.ok(new ApiResponse("Video added successfully", true));
    }

    // GET metod för att hämta alla videor
    @GetMapping
    public ResponseEntity<List<VideoWithUserDTO>> getAllVideos() {
        List<VideoWithUserDTO> videos = videoService.findAllWithUser();
        return ResponseEntity.ok(videos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoDTO> getVideoById(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.get(id));
    }

    @GetMapping("/{videoId}/like")
    public ResponseEntity<Long> getLikes(@PathVariable Long videoId) {
        long likeCount = likeService.countLikes(videoId);
        return ResponseEntity.ok(likeCount);
    }

    @GetMapping("/{videoId}/dislike")
    public ResponseEntity<Long> getDislikes(@PathVariable Long videoId) {
        long dislikeCount = likeService.countDislikes(videoId);
        return ResponseEntity.ok(dislikeCount);
    }

    @GetMapping("/{videoId}/status/{userId}")
    public ResponseEntity<Map<String, Boolean>> getUserLikeStatus(@PathVariable Long videoId, @PathVariable Long userId) {
        boolean isLiked = likeService.isVideoLikedByUser(videoId, userId);
        boolean isDisliked = likeService.isVideoDislikedByUser(videoId, userId);
        Map<String, Boolean> status = new HashMap<>();
        status.put("isLiked", isLiked);
        status.put("isDisliked", isDisliked);
        return ResponseEntity.ok(status);
    }

}


