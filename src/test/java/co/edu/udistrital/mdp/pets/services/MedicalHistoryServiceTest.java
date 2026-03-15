package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({MedicalHistoryService.class})
class MedicalHistoryServiceTest {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<VeterinarianEntity> vetList = new ArrayList<>();
    private final List<PetEntity> petList = new ArrayList<>();
    private final List<MedicalHistoryEntity> historyList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalHistoryEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        for (int i = 0; i < 3; i++) {
            VeterinarianEntity vet = new VeterinarianEntity();
            vet.setName("vet" + i);
            vet.setEmail("vet" + i + "@mail.com");
            vet.setPassword("pass");
            vet.setPhoneNumber("12345");
            vet.setShelter(shelter);
            entityManager.persist(vet);
            vetList.add(vet);
        }

        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            pet.setStatus("IN_SHELTER");
            entityManager.persist(pet);
            petList.add(pet);
        }

        for (int i = 0; i < 2; i++) {
            MedicalHistoryEntity history = factory.manufacturePojo(MedicalHistoryEntity.class);
            history.setPet(petList.get(i));
            history.setVeterinarian(vetList.get(i));
            history.setCreatedDate(LocalDate.now());
            history.setLastUpdated(LocalDate.now());
            entityManager.persist(history);
            historyList.add(history);
        }
    }

    @Test
    void testCreateMedicalHistory() throws Exception {
        // Find a pet without history, which is petList.get(2)
        PetEntity pet = petList.get(2);
        VeterinarianEntity vet = vetList.get(0);

        MedicalHistoryEntity result = medicalHistoryService.createMedicalHistory(pet.getId(), vet.getId());

        assertNotNull(result);
        MedicalHistoryEntity stored = entityManager.find(MedicalHistoryEntity.class, result.getId());
        assertEquals(pet.getId(), stored.getPet().getId());
        assertEquals(vet.getId(), stored.getVeterinarian().getId());
        assertNotNull(stored.getCreatedDate());
    }

    @Test
    void testCreateMedicalHistoryPetNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> medicalHistoryService.createMedicalHistory(0L, vetList.get(0).getId())));
    }

    @Test
    void testCreateMedicalHistoryVetNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> medicalHistoryService.createMedicalHistory(petList.get(2).getId(), 0L)));
    }

    @Test
    void testSearchMedicalHistory() throws Exception {
        MedicalHistoryEntity expected = historyList.get(0);
        MedicalHistoryEntity result = medicalHistoryService.searchMedicalHistory(expected.getPet().getId());

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchMedicalHistoryNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> medicalHistoryService.searchMedicalHistory(0L)));
    }

    @Test
    void testSearchMedicalHistories() {
        List<MedicalHistoryEntity> results = medicalHistoryService.searchMedicalHistories();
        assertTrue(results.size() >= historyList.size());
    }

    @Test
    void testUpdateMedicalHistory() throws Exception {
        MedicalHistoryEntity history = historyList.get(0);
        LocalDate oldDate = LocalDate.now().minusDays(5);
        history.setLastUpdated(oldDate);
        entityManager.persist(history);

        MedicalHistoryEntity result = medicalHistoryService.updateMedicalHistory(history.getId());

        MedicalHistoryEntity stored = entityManager.find(MedicalHistoryEntity.class, history.getId());
        assertTrue(stored.getLastUpdated().isAfter(oldDate) || stored.getLastUpdated().isEqual(LocalDate.now()));
    }

    @Test
    void testUpdateMedicalHistoryNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> medicalHistoryService.updateMedicalHistory(0L)));
    }

    @Test
    void testDeleteMedicalHistoryAdopted() throws Exception {
        MedicalHistoryEntity history = historyList.get(0);
        PetEntity pet = history.getPet();
        pet.setStatus("ADOPTED");
        entityManager.persist(pet);

        medicalHistoryService.deleteMedicalHistory(history.getId());

        MedicalHistoryEntity deleted = entityManager.find(MedicalHistoryEntity.class, history.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteMedicalHistoryNotAdopted() throws Exception {
        MedicalHistoryEntity history = historyList.get(0);
        PetEntity pet = history.getPet();
        pet.setStatus("IN_SHELTER");
        entityManager.persist(pet);

        assertNotNull(assertThrows(IllegalOperationException.class, 
            () -> medicalHistoryService.deleteMedicalHistory(history.getId())));
    }

    @Test
    void testDeleteMedicalHistoryNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> medicalHistoryService.deleteMedicalHistory(0L)));
    }
}
