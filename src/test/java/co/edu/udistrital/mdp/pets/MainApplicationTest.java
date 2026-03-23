package co.edu.udistrital.mdp.pets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTest {

    @Test
    void contextLoads() {
        org.junit.jupiter.api.Assertions.assertTrue(true);
        // Just verify context loads
    }

    @Test
    void mainMethod() {
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> {
            MainApplication.main(new String[]{"--server.port=0"});
        });
    }
}
