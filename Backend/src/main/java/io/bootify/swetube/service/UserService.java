package io.bootify.swetube.service;

import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video; // Uppdatera importen för entiteten
import io.bootify.swetube.model.UserDTO;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.repos.VideoRepository; // Uppdatera importen för repository
import io.bootify.swetube.util.NotFoundException;
import io.bootify.swetube.util.ReferencedWarning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final VideoRepository videoRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final VideoRepository videoRepository,
                       final PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.videoRepository = videoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Lägg till findById metod för att hämta användare baserat på ID
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Long id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final UserDTO userDTO) {
        if (isEmailTaken(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (isUsernameTaken(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }

        final User user = new User();
        mapToEntity(userDTO, user);
        // Kryptera lösenord innan det sparas
        user.setUserPassword(passwordEncoder.encode(userDTO.getPassword()));
        if (user.getUserRoles() == null || user.getUserRoles().isEmpty()) {
            user.setUserRoles("USER");
        }
        return userRepository.save(user).getUserId();
    }

    public void update(final Long id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        // Kryptera lösenord om det är ändrat
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userRepository.save(user);
    }

    public void delete(final Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getUserId());
        userDTO.setUsername(user.getUserName());
        userDTO.setPassword(null); // Skicka aldrig tillbaka lösenord
        userDTO.setRole(user.getUserRoles());
        userDTO.setEmail(user.getUserEmail());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUserName(userDTO.getUsername());
        user.setUserEmail(userDTO.getEmail());
        if (userDTO.getRole() != null) {
            user.setUserRoles(userDTO.getRole());
        }
        return user;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Video userVideo = videoRepository.findFirstByUser(user);
        if (userVideo != null) {
            referencedWarning.setKey("user.video.user.referenced");
            referencedWarning.addParam(userVideo.getUser());
            return referencedWarning;
        }
        return null;
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByUserEmail(email);
    }

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUserName(username);
    }
}