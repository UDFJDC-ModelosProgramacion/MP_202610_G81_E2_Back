package co.edu.udistrital.mdp.pets.controllers;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DefaultControllerTest {

    @Test
    void testWelcome() {
        DefaultController controller = new DefaultController();
        Map<String, String> response = controller.welcome();
        
        assertNotNull(response);
        assertEquals("OK", response.get("status"));
        assertEquals("REST API for Pets is running", response.get("message"));
    }
}
