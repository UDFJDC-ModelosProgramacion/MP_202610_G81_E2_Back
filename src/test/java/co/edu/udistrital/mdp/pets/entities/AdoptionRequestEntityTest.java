package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class AdoptionRequestEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private AdoptionRequestEntity requestEntity;

    @BeforeEach
    void setUp() {
        requestEntity = factory.manufacturePojo(AdoptionRequestEntity.class);
    }

    @Test
    void testCreateAdoptionRequest() {
        AdoptionRequestEntity saved = entityManager.persistAndFlush(requestEntity);
        AdoptionRequestEntity found = entityManager.find(AdoptionRequestEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(requestEntity.getRequestDate(), found.getRequestDate());
        assertEquals(requestEntity.getStatus(), found.getStatus());
        assertEquals(requestEntity.getComments(), found.getComments());
    }

    @Test
    void testAdoptionRequestWithAdopter() {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persistAndFlush(adopter);

        requestEntity.setAdopter(adopter);
        AdoptionRequestEntity saved = entityManager.persistAndFlush(requestEntity);

        AdoptionRequestEntity found = entityManager.find(AdoptionRequestEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getAdopter());
        assertEquals(adopter.getId(), found.getAdopter().getId());
    }
}
