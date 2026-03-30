package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication credentials.
 * Used for login/token-based authentication flows.
 */
@Data
@NoArgsConstructor
public class AuthenticationDTO {
    private String email;
    private String password;
}
