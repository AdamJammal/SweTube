package io.bootify.swetube.repos;

import io.bootify.swetube.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserEmail(String userEmail); // Kontrollera om e-posten redan finns

    boolean existsByUserName(String username);
    Optional<User> findByUserName(String userName); // Ändra från `findByUsername` till `findByUserName`
}
