package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class ShelterEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ShelterEntity shelterEntity;

    @BeforeEach
    void setUp() {
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
    }

    @Test
    void testCreateShelter() {
        ShelterEntity saved = entityManager.persistAndFlush(shelterEntity);
        ShelterEntity found = entityManager.find(ShelterEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(shelterEntity.getShelterName(), found.getShelterName());
        assertEquals(shelterEntity.getCity(), found.getCity());
        assertEquals(shelterEntity.getLocationDetails(), found.getLocationDetails());
        assertEquals(shelterEntity.getDescription(), found.getDescription());
        assertEquals(shelterEntity.getWebsiteUrl(), found.getWebsiteUrl());
    }

    @Test
    void testShelterWithPets() {
        ShelterEntity saved = entityManager.persistAndFlush(shelterEntity);

        PetEntity pet1 = factory.manufacturePojo(PetEntity.class);
        pet1.setShelter(saved);
        entityManager.persistAndFlush(pet1);

        PetEntity pet2 = factory.manufacturePojo(PetEntity.class);
        pet2.setShelter(saved);
        entityManager.persistAndFlush(pet2);

        entityManager.clear();

        ShelterEntity found = entityManager.find(ShelterEntity.class, saved.getId());
        assertNotNull(found);
    }

    @Test
    void testShelterWithMedia() {
        ShelterEntity saved = entityManager.persistAndFlush(shelterEntity);

        ShelterMediaEntity media = factory.manufacturePojo(ShelterMediaEntity.class);
        media.setShelter(saved);
        saved.getMediaItems().add(media);
        entityManager.persistAndFlush(media);
        entityManager.merge(saved);
        entityManager.flush();

        entityManager.clear();

        ShelterEntity found = entityManager.find(ShelterEntity.class, saved.getId());
        assertNotNull(found);
        assertFalse(found.getMediaItems().isEmpty());
        assertEquals(1, found.getMediaItems().size());
    }
}
