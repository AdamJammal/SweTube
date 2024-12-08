package io.bootify.swetube.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, length = 255)
    private String userName;

    @Column(nullable = false, unique = true, length = 255)
    private String userEmail;

    @Column(nullable = false, length = 255)
    private String userPassword;

    @Column(length = 255)
    private String userRoles;

    private LocalDate userCreatedAt;

    @Column(length = 255)
    private String avatarUrl; // Ny kolumn för avatar URL

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore // Förhindrar att "videos" serialiseras
    private Set<Video> videos; // Kopplar videor till användaren
}