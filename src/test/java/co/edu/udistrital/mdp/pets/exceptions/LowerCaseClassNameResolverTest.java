package co.edu.udistrital.mdp.pets.exceptions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LowerCaseClassNameResolverTest {

    @Test
    void testResolver() {
        LowerCaseClassNameResolver resolver = new LowerCaseClassNameResolver();
        
        String id1 = resolver.idFromValue("A String");
        assertEquals("string", id1);
        
        String id2 = resolver.idFromValueAndType(123, Integer.class);
        assertEquals("integer", id2);
        
        assertEquals(JsonTypeInfo.Id.CUSTOM, resolver.getMechanism());
    }
}
