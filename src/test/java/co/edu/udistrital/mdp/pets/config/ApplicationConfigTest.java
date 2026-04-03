package co.edu.udistrital.mdp.pets.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

class ApplicationConfigTest {

    private final ApplicationConfig config = new ApplicationConfig();

    @Test
    void testCorsConfigurerBeanIsNotNull() {
        WebMvcConfigurer configurer = config.corsConfigurer();
        assertNotNull(configurer);
    }

    @Test
    void testCorsConfigurerAddsMappings() {
        WebMvcConfigurer configurer = config.corsConfigurer();
        CorsRegistry registry = new CorsRegistry();
        configurer.addCorsMappings(registry);
        assertNotNull(registry);
    }
}
