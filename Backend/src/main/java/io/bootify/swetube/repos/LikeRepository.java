package io.bootify.swetube.repos;

import io.bootify.swetube.domain.Like;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByVideoAndUser(Video video, User user);
    long countByVideoAndIsLiked(Video video, boolean isLiked);
}
