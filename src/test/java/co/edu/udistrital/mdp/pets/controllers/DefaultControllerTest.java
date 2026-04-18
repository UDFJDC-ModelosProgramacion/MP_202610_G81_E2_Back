package co.edu.udistrital.mdp.pets.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import co.edu.udistrital.mdp.pets.dto.DefaultDetailDTO;

class DefaultControllerTest {

    @Test
    void testWelcome() {
        DefaultController controller = new DefaultController(new MockEnvironment());
        DefaultDetailDTO response = controller.welcome();
        
        assertNotNull(response);
        assertEquals("OK", response.getStatus());
        assertEquals("REST API for Pets is running", response.getMessage());
    }

    @Test
    void testWelcomeWithConfiguredApplicationName() {
        MockEnvironment environment = new MockEnvironment().withProperty("spring.application.name", "ApiMascotas");
        DefaultController controller = new DefaultController(environment);
        DefaultDetailDTO response = controller.welcome();

        assertEquals("REST API for ApiMascotas is running", response.getMessage());
    }
}
