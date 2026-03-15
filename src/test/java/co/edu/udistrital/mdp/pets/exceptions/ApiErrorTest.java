package co.edu.udistrital.mdp.pets.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ApiErrorTest {

    @Test
    void testApiError() {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND);
        error.setMessage("Message");
        assertEquals(HttpStatus.NOT_FOUND, error.getStatus());
        assertEquals("Message", error.getMessage());
        assertNotNull(error.getTimestamp());
        
        // cover lombok methods
        error.setStatus(HttpStatus.BAD_REQUEST);
        assertEquals(HttpStatus.BAD_REQUEST, error.getStatus());
        
        String toString = error.toString();
        assertNotNull(toString);
        
        ApiError error2 = new ApiError(HttpStatus.BAD_REQUEST);
        error2.setMessage("Message");
        
        LocalDateTime t = LocalDateTime.now();
        error.setTimestamp(t);
        error2.setTimestamp(t);
        
        assertEquals(error, error2);
        assertEquals(error.hashCode(), error2.hashCode());
    }
}
