package io.bootify.swetube.rest;

import io.bootify.swetube.model.CommentDTO;
import io.bootify.swetube.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return ResponseEntity.ok(commentService.create(commentDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam Long userId) {
        commentService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByVideo(@PathVariable Long videoId) {
        return ResponseEntity.ok(commentService.findByVideo(videoId));
    }
}