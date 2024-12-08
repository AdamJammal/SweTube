package io.bootify.swetube.service;

import io.bootify.swetube.domain.Comment;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import io.bootify.swetube.model.CommentDTO;
import io.bootify.swetube.repos.CommentRepository;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.repos.VideoRepository;
import io.bootify.swetube.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final VideoRepository videoRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, VideoRepository videoRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.videoRepository = videoRepository;
        this.userRepository = userRepository;
    }

    public CommentDTO create(CommentDTO commentDTO) {
        Video video = videoRepository.findById(commentDTO.getVideoId())
                .orElseThrow(() -> new NotFoundException("Video not found"));
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Comment comment = new Comment();
        comment.setVideo(video);
        comment.setUser(user);
        comment.setText(commentDTO.getText());
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);
        return mapToDTO(savedComment);
    }

    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    public List<CommentDTO> findByVideo(Long videoId) {
        List<Comment> comments = commentRepository.findByVideoIdWithUserAndVideo(videoId);
        return comments.stream().map(this::mapToDTO).collect(Collectors.toList());
    }


    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUser().getUserId());
        dto.setVideoId(comment.getVideo().getVideoId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUserName(comment.getUser().getUserName());
        dto.setAvatarUrl(comment.getUser().getAvatarUrl());
        return dto;
    }
}
