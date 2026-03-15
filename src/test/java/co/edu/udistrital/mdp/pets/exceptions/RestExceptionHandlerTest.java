package co.edu.udistrital.mdp.pets.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestExceptionHandlerTest {

    @Test
    void testHandleEntityNotFound() {
        RestExceptionHandler handler = new RestExceptionHandler();
        EntityNotFoundException ex = new EntityNotFoundException("Not found message");
        
        ResponseEntity<Object> response = handler.handleEntityNotFound(ex);
        
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Object body = response.getBody();
        assertNotNull(body);
        ApiError apiError = (ApiError) body;
        assertEquals("Not found message", apiError.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, apiError.getStatus());
    }

    @Test
    void testHandleIllegalOperation() {
        RestExceptionHandler handler = new RestExceptionHandler();
        IllegalOperationException ex = new IllegalOperationException("Illegal operation message");
        
        ResponseEntity<Object> response = handler.handleIllegalOperation(ex);
        
        assertEquals(HttpStatus.PRECONDITION_FAILED, response.getStatusCode());
        Object body = response.getBody();
        assertNotNull(body);
        ApiError apiError = (ApiError) body;
        assertEquals("Illegal operation message", apiError.getMessage());
        assertEquals(HttpStatus.PRECONDITION_FAILED, apiError.getStatus());
    }
}
