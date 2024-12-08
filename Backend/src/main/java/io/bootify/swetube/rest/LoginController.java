package io.bootify.swetube.rest;

import io.bootify.swetube.domain.LoginRequest;
import io.bootify.swetube.domain.LoginResponse;
import io.bootify.swetube.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for username: {}", request.getUsername());
        try {
            // Hämta LoginResponse som innehåller både token, userName och userId
            LoginResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response); // Skicka tillbaka hela LoginResponse
        } catch (ResponseStatusException e) {
            HttpStatus status = (HttpStatus) e.getStatusCode();
            log.warn("Login failed: {} - {}", status, e.getReason());
            return ResponseEntity.status(status).body(e.getReason());
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ett oväntat fel inträffade");
        }
    }
}