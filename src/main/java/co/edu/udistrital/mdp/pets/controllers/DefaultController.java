package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.DefaultDetailDTO;

@RestController
@RequestMapping("/")
public class DefaultController {
    private final Environment environment;

    public DefaultController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public DefaultDetailDTO welcome() {
        DefaultDetailDTO response = new DefaultDetailDTO();
        response.setStatus("OK");
        String applicationName = environment.getProperty("spring.application.name", "Pets");
        response.setMessage("REST API for " + applicationName + " is running");
        return response;
    }
}
