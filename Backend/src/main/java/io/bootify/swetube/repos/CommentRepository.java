package io.bootify.swetube.repos;

import io.bootify.swetube.domain.Comment;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByVideo(Video video);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user u JOIN FETCH c.video v WHERE v.videoId = :videoId")
    List<Comment> findByVideoIdWithUserAndVideo(@Param("videoId") Long videoId);
}
