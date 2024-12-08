package io.bootify.swetube.repos;

import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Video findFirstByUser(User user);
    // Hämta alla videos med användardata (user) utan att använda custom query

    @EntityGraph(attributePaths = "user") // Detta gör att "user"-relationen hämtas tillsammans med Video
    List<Video> findAll();
}
