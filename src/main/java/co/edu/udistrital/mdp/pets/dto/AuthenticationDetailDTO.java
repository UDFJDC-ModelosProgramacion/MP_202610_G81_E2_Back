package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for Authentication response, including a token and basic user info.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AuthenticationDetailDTO extends AuthenticationDTO {
    private Long userId;
    private String name;
    private String token;
    private String role;
}
