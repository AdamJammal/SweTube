package io.bootify.swetube.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String userName; // Lägg till användarnamn
    private Long userId; // Lägg till userId här
}
