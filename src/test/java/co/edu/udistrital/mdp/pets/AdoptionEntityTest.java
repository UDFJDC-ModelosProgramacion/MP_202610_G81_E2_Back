package co.edu.udistrital.mdp.pets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class AdoptionEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private AdoptionEntity adoptionEntity;

    @BeforeEach
    void setUp() {
        adoptionEntity = factory.manufacturePojo(AdoptionEntity.class);
    }

    @Test
    void testCreateAdoption() {
        AdoptionEntity saved = entityManager.persistAndFlush(adoptionEntity);
        AdoptionEntity found = entityManager.find(AdoptionEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(adoptionEntity.getOfficialDate(), found.getOfficialDate());
        assertEquals(adoptionEntity.getContractSigned(), found.getContractSigned());
    }

    @Test
    void testAdoptionWithPet() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persistAndFlush(pet);

        adoptionEntity.setPet(pet);
        AdoptionEntity saved = entityManager.persistAndFlush(adoptionEntity);

        AdoptionEntity found = entityManager.find(AdoptionEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getPet());
        assertEquals(pet.getId(), found.getPet().getId());
    }
}
