package co.edu.udistrital.mdp.pets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        // Permitir cualquier puerto en localhost para desarrollo
                        .allowedOriginPatterns("http://localhost:*", "http://127.0.0.1:*", "http://192.168.68.*:*")
                        // Agregamos OPTIONS (vital para CORS) y PATCH
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                        .allowedHeaders("*")
                        .exposedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }

        };
    }
}