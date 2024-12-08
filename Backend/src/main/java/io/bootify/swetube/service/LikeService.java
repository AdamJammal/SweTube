package io.bootify.swetube.service;

import io.bootify.swetube.domain.Like;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import io.bootify.swetube.repos.LikeRepository;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.repos.VideoRepository;
import io.bootify.swetube.util.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public LikeService(LikeRepository likeRepository, VideoRepository videoRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public long likeVideo(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Like like = likeRepository.findByVideoAndUser(video, user).orElse(new Like());
        like.setVideo(video);
        like.setUser(user);
        like.setIsLiked(true);
        likeRepository.save(like);

        return updateVideoLikes(video);
    }

    @Transactional
    public long dislikeVideo(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Like like = likeRepository.findByVideoAndUser(video, user).orElse(new Like());
        like.setVideo(video);
        like.setUser(user);
        like.setIsLiked(false);
        likeRepository.save(like);

        return updateVideoDislikes(video);
    }

    public long countLikes(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        return likeRepository.countByVideoAndIsLiked(video, true); // Antal likes
    }


    public long countDislikes(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        return likeRepository.countByVideoAndIsLiked(video, false);
    }

    public boolean isVideoLikedByUser(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return likeRepository.findByVideoAndUser(video, user)
                .map(Like::getIsLiked)
                .orElse(false);
    }

    public boolean isVideoDislikedByUser(Long videoId, Long userId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new NotFoundException("Video not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return likeRepository.findByVideoAndUser(video, user)
                .map(like -> !like.getIsLiked())
                .orElse(false);
    }

    @Transactional
    public long updateVideoLikes(Video video) {
        long totalLikes = likeRepository.countByVideoAndIsLiked(video, true);
        video.setVideoLikes(totalLikes);
        videoRepository.save(video);
        return totalLikes;
    }

    @Transactional
    public long updateVideoDislikes(Video video) {
        long totalDislikes = likeRepository.countByVideoAndIsLiked(video, false);
        video.setVideoDislikes(totalDislikes);
        videoRepository.save(video);
        return totalDislikes;
    }

}
