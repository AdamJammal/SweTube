package io.bootify.swetube.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

    @Size(max = 255) // Om "role" inte är obligatoriskt
    private String role;

    @NotBlank
    @Size(max = 255)
    private String email;
}