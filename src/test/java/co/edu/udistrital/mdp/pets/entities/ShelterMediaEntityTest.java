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
class ShelterMediaEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ShelterMediaEntity mediaEntity;

    @BeforeEach
    void setUp() {
        mediaEntity = factory.manufacturePojo(ShelterMediaEntity.class);
    }

    @Test
    void testCreateShelterMedia() {
        ShelterMediaEntity saved = entityManager.persistAndFlush(mediaEntity);
        ShelterMediaEntity found = entityManager.find(ShelterMediaEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(mediaEntity.getMediaUrl(), found.getMediaUrl());
        assertEquals(mediaEntity.getMediaType(), found.getMediaType());
        assertEquals(mediaEntity.getDescription(), found.getDescription());
    }

    @Test
    void testShelterMediaWithShelter() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persistAndFlush(shelter);

        mediaEntity.setShelter(shelter);
        ShelterMediaEntity saved = entityManager.persistAndFlush(mediaEntity);

        ShelterMediaEntity found = entityManager.find(ShelterMediaEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getShelter());
        assertEquals(shelter.getId(), found.getShelter().getId());
        assertEquals(shelter.getShelterName(), found.getShelter().getShelterName());
    }
}
