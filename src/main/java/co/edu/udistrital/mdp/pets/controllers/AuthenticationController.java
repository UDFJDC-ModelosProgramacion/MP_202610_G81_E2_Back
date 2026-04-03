package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AuthenticationDTO;
import co.edu.udistrital.mdp.pets.dto.AuthenticationDetailDTO;

/**
 * Controller for user authentication (login).
 * NOTE: Full security implementation (e.g. JWT) is out of scope here;
 * this controller provides the DTO surface for the auth flow.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    /**
     * Login endpoint.
     * Returns a stub AuthenticationDetailDTO — wire to a real AuthService/JWT provider when ready.
     */
    @PostMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public AuthenticationDetailDTO login(@RequestBody AuthenticationDTO authDTO) {
        // Stub: real implementation should validate credentials and generate a JWT.
        AuthenticationDetailDTO response = new AuthenticationDetailDTO();
        response.setEmail(authDTO.getEmail());
        response.setToken("stub-token-replace-with-jwt");
        return response;
    }
}
