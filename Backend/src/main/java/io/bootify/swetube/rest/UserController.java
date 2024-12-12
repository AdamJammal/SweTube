package io.bootify.swetube.rest;

import io.bootify.swetube.domain.ApiResponse;
import io.bootify.swetube.model.ChangePasswordDTO;
import io.bootify.swetube.model.UserDTO;
import io.bootify.swetube.service.UserService;
import io.bootify.swetube.util.NotFoundException;
import io.bootify.swetube.util.ReferencedException;
import io.bootify.swetube.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:4200") // Tillåt Angular-frontend
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createUser(@RequestBody @Valid final UserDTO userDTO) {
        final Long createdId = userService.create(userDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateUser(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final UserDTO userDTO) {
        userService.update(id, userDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = userService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }


    // Hämtar användarprofilen baserat på användarens ID
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(@RequestParam Long userId) {
        try {
            // Försök att hämta användarprofilen
            UserDTO userDTO = userService.getUserProfile(userId);
            return ResponseEntity.ok(userDTO);
        } catch (NotFoundException e) {
            // Om användaren inte hittades, returnera 404 Not Found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserDTO()); // Eller en custom ApiResponse med error info
        } catch (Exception e) {
            // Om något annat fel uppstår, returnera 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Här kan du returnera en felbeskrivning i form av ApiResponse eller UserDTO
        }
    }

    // Uppdaterar användarprofilen
    @PutMapping("/profile/update")
    public ResponseEntity<ApiResponse> updateUserProfileWithoutPassword(@RequestBody @Valid UserDTO userDTO) {
        try {
            ApiResponse response = userService.updateUserProfileWithoutPassword(userDTO);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Returnera ett Bad Request med ett specifikt felmeddelande om e-posten redan är tagen
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            // Fångar andra oväntade fel och skickar ett generellt felmeddelande
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", false));
        }
    }

    @PutMapping("/profile/change-password")
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) {
        ApiResponse response = userService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(response);
    }
}
