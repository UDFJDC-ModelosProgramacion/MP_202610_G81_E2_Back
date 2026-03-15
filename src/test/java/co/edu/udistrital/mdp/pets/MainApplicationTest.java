package co.edu.udistrital.mdp.pets;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MainApplicationTest {

    @Test
    void contextLoads() {
        // Just verify context loads
    }

    @Test
    void mainMethod() {
        MainApplication.main(new String[]{});
    }
}
