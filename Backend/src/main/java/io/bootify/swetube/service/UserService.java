package io.bootify.swetube.service;

import io.bootify.swetube.domain.ApiResponse;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.domain.Video; // Uppdatera importen för entiteten
import io.bootify.swetube.model.ChangePasswordDTO;
import io.bootify.swetube.model.UserDTO;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.repos.VideoRepository; // Uppdatera importen för repository
import io.bootify.swetube.util.NotFoundException;
import io.bootify.swetube.util.ReferencedWarning;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

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

    // Hämtar användarprofil baserat på userId
    public UserDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        return modelMapper.map(user, UserDTO.class);
    }


    public ApiResponse updateUserProfileWithoutPassword(UserDTO userDTO) {
        // Kontrollera om e-postadressen redan är tagen av en annan användare
        if (isEmailTakenByAnotherUser(userDTO.getEmail(), userDTO.getId())) {
            throw new IllegalArgumentException("Email is already taken by another user");
        }

        User user = userRepository.findById(userDTO.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Uppdatera användardata, men sätt inte lösenordet om det inte är ändrat
        user.setUserName(userDTO.getUsername());
        user.setUserEmail(userDTO.getEmail());  // Uppdaterar e-postadressen
        user.setUserRoles(userDTO.getRole());

        // Om lösenordet inte är null, uppdatera det
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

        return new ApiResponse("User profile updated successfully", true);
    }

    public ApiResponse changePassword(ChangePasswordDTO changePasswordDTO) {
        // Hämta användaren baserat på userId från DTO
        User user = userRepository.findById(changePasswordDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Kontrollera om det nuvarande lösenordet är korrekt
        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getUserPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Uppdatera lösenordet
        user.setUserPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);

        return new ApiResponse("Password updated successfully", true);
    }

// Kontrollera om e-postadressen är tagen av en annan användare
    private boolean isEmailTakenByAnotherUser(String email, Long userId) {
        // Kontrollera om e-postadressen finns i databasen, och se till att den inte tillhör den aktuella användaren
        return userRepository.existsByUserEmail(email) &&
                userRepository.findByUserEmail(email)
                        .map(user -> !user.getUserId().equals(userId))
                        .orElse(false);
    }

}