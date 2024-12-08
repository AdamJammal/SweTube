package io.bootify.swetube.service;

import io.bootify.swetube.domain.LoginRequest;
import io.bootify.swetube.domain.LoginResponse;
import io.bootify.swetube.domain.User;
import io.bootify.swetube.repos.UserRepository;
import io.bootify.swetube.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
    public class AuthenticationService {

        private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        public LoginResponse authenticate(LoginRequest request) {
            log.info("Authenticating user: {}", request.getUsername());

            User user = userRepository.findByUserName(request.getUsername())
                    .orElseThrow(() -> {
                        log.warn("User not found: {}", request.getUsername());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                    });

            log.info("User found: {}", user.getUserName());

            if (passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {
                log.info("Authentication successful for user: {}", request.getUsername());

                String token = JwtUtil.generateToken(user); // Generera JWT-token
                Long userId = user.getUserId(); // Hämta användarens ID (Long)

                // Returnera LoginResponse med token, userName och userId
                return new LoginResponse(token, user.getUserName(), userId);
            } else {
                log.warn("Invalid password for user: {}", request.getUsername());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
            }
        }
    }


