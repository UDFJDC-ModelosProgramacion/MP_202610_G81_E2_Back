package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class PetEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private PetEntity petEntity;

    @BeforeEach
    void setUp() {
        petEntity = factory.manufacturePojo(PetEntity.class);
    }

    @Test
    void testCreatePet() {
        PetEntity saved = entityManager.persistAndFlush(petEntity);
        PetEntity found = entityManager.find(PetEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(petEntity.getName(), found.getName());
        assertEquals(petEntity.getSpecies(), found.getSpecies());
        assertEquals(petEntity.getBreed(), found.getBreed());
        assertEquals(petEntity.getBornDate(), found.getBornDate());
        assertEquals(petEntity.getSex(), found.getSex());
        assertEquals(petEntity.getSize(), found.getSize());
        assertEquals(petEntity.getTemperament(), found.getTemperament());
        assertEquals(petEntity.getSpecificNeeds(), found.getSpecificNeeds());
        assertEquals(petEntity.getIsRescued(), found.getIsRescued());
        assertEquals(petEntity.getStatus(), found.getStatus());
    }

    @Test
    void testPetWithShelter() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persistAndFlush(shelter);

        petEntity.setShelter(shelter);
        PetEntity saved = entityManager.persistAndFlush(petEntity);

        PetEntity found = entityManager.find(PetEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getShelter());
        assertEquals(shelter.getId(), found.getShelter().getId());
        assertEquals(shelter.getShelterName(), found.getShelter().getShelterName());
    }
}
