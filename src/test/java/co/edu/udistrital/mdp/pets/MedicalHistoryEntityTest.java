package co.edu.udistrital.mdp.pets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class MedicalHistoryEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private MedicalHistoryEntity medicalHistoryEntity;

    @BeforeEach
    void setUp() {
        medicalHistoryEntity = factory.manufacturePojo(MedicalHistoryEntity.class);
    }

    @Test
    void testCreateMedicalHistory() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persistAndFlush(pet);

        medicalHistoryEntity.setPet(pet);
        MedicalHistoryEntity saved = entityManager.persistAndFlush(medicalHistoryEntity);

        MedicalHistoryEntity found = entityManager.find(MedicalHistoryEntity.class, saved.getId());
        assertNotNull(found);
        assertEquals(medicalHistoryEntity.getCreatedDate(), found.getCreatedDate());
        assertEquals(medicalHistoryEntity.getLastUpdated(), found.getLastUpdated());
        assertNotNull(found.getPet());
    }

    @Test
    void testMedicalHistoryWithEvents() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persistAndFlush(pet);

        medicalHistoryEntity.setPet(pet);
        MedicalHistoryEntity saved = entityManager.persistAndFlush(medicalHistoryEntity);

        MedicalEventEntity event = factory.manufacturePojo(MedicalEventEntity.class);
        event.setMedicalHistory(saved);
        entityManager.persistAndFlush(event);

        entityManager.clear();

        MedicalHistoryEntity found = entityManager.find(MedicalHistoryEntity.class, saved.getId());
        assertNotNull(found);
        assertFalse(found.getMedicalEvents().isEmpty());
        assertEquals(1, found.getMedicalEvents().size());
    }

    @Test
    void testMedicalHistoryWithVaccines() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persistAndFlush(pet);

        medicalHistoryEntity.setPet(pet);
        MedicalHistoryEntity saved = entityManager.persistAndFlush(medicalHistoryEntity);

        VaccineEntryEntity vaccine = factory.manufacturePojo(VaccineEntryEntity.class);
        vaccine.setMedicalHistory(saved);
        entityManager.persistAndFlush(vaccine);

        entityManager.clear();

        MedicalHistoryEntity found = entityManager.find(MedicalHistoryEntity.class, saved.getId());
        assertNotNull(found);
        assertFalse(found.getVaccineEntries().isEmpty());
        assertEquals(1, found.getVaccineEntries().size());
    }
}
