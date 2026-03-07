package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;

@DataJpaTest
class ShelterEntityTest {

    @Autowired
    private ShelterRepository shelterRepository;

    private ShelterEntity shelter;

    @BeforeEach
    void setUp() {
        shelter = new ShelterEntity();
        shelter.setShelterName("Refugio Pelitos Felices");
        shelter.setCity("Bogotá");
        shelter.setLocationDetails("Calle 100 # 15-30, Barrio El Refugio");
        shelter.setDescription("Refugio dedicado al cuidado y adopción de mascotas");
        shelter.setWebsiteUrl("https://pelitosfelices.co");
    }

    @Test
    void testPersistShelterEntity() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertNotNull(saved);
        assertNotNull(saved.getId());
    }

    @Test
    void testShelterNameIsPersisted() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertEquals("Refugio Pelitos Felices", saved.getShelterName());
    }

    @Test
    void testShelterCityIsPersisted() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertEquals("Bogotá", saved.getCity());
    }

    @Test
    void testShelterLocationDetailsIsPersisted() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertEquals("Calle 100 # 15-30, Barrio El Refugio", saved.getLocationDetails());
    }

    @Test
    void testShelterDescriptionIsPersisted() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertEquals("Refugio dedicado al cuidado y adopción de mascotas", saved.getDescription());
    }

    @Test
    void testShelterWebsiteUrlIsPersisted() {
        ShelterEntity saved = shelterRepository.save(shelter);
        assertEquals("https://pelitosfelices.co", saved.getWebsiteUrl());
    }

    @Test
    void testFindShelterById() {
        ShelterEntity saved = shelterRepository.save(shelter);
        ShelterEntity found = shelterRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getShelterName(), found.getShelterName());
    }
}
