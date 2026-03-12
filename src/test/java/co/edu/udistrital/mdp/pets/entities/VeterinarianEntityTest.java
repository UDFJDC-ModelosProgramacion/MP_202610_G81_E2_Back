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
class VeterinarianEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private VeterinarianEntity vetEntity;

    @BeforeEach
    void setUp() {
        vetEntity = factory.manufacturePojo(VeterinarianEntity.class);
    }

    @Test
    void testCreateVeterinarian() {
        VeterinarianEntity saved = entityManager.persistAndFlush(vetEntity);
        VeterinarianEntity found = entityManager.find(VeterinarianEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(vetEntity.getLicenseNumber(), found.getLicenseNumber());
        assertEquals(vetEntity.getAvailabilitySchedule(), found.getAvailabilitySchedule());
        assertEquals(vetEntity.getName(), found.getName());
    }

    @Test
    void testVeterinarianInheritsUserFields() {
        VeterinarianEntity saved = entityManager.persistAndFlush(vetEntity);
        VeterinarianEntity found = entityManager.find(VeterinarianEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(vetEntity.getEmail(), found.getEmail());
        assertEquals(vetEntity.getPhoneNumber(), found.getPhoneNumber());
    }

    @Test
    void testVeterinarianWithShelter() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persistAndFlush(shelter);

        vetEntity.setShelter(shelter);
        VeterinarianEntity saved = entityManager.persistAndFlush(vetEntity);

        VeterinarianEntity found = entityManager.find(VeterinarianEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getShelter());
        assertEquals(shelter.getId(), found.getShelter().getId());
    }

    @Test
    void testVeterinarianWithSpecialities() {
        VetSpecialityEntity speciality = factory.manufacturePojo(VetSpecialityEntity.class);
        entityManager.persistAndFlush(speciality);

        vetEntity.getSpecialities().add(speciality);
        VeterinarianEntity saved = entityManager.persistAndFlush(vetEntity);

        entityManager.clear();

        VeterinarianEntity found = entityManager.find(VeterinarianEntity.class, saved.getId());
        assertNotNull(found);
        assertFalse(found.getSpecialities().isEmpty());
        assertEquals(1, found.getSpecialities().size());
    }
}
