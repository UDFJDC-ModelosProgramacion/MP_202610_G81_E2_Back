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

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({TrialCohabitationService.class})
class TrialCohabitationServiceTest {

    @Autowired
    private TrialCohabitationService trialCohabitationService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<TrialCohabitationEntity> trialList = new ArrayList<>();
    private AdoptionRequestEntity adoptionRequest;

    private <T extends Throwable> void expectThrows(Class<T> expectedType,
            org.junit.jupiter.api.function.Executable executable) {
        assertNotNull(assertThrows(expectedType, executable));
    }

    @BeforeEach
    @SuppressWarnings({"java:S1144", "unused"})
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from TrialCohabitationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdopterEntity adopter = new AdopterEntity();
        adopter.setName("Test Adopter");
        adopter.setEmail("adoptertrial@test.com");
        adopter.setPassword("pass");
        adopter.setPhoneNumber("123");
        adopter.setAddress("Calle 123");
        entityManager.persist(adopter);

        for (int i = 0; i < 3; i++) {
            AdoptionRequestEntity req = factory.manufacturePojo(AdoptionRequestEntity.class);
            req.setPet(pet);
            req.setAdopter(adopter);
            req.setRequestDate(LocalDate.now());
            entityManager.persist(req);

            TrialCohabitationEntity trial = new TrialCohabitationEntity();
            trial.setCreationDate(LocalDate.now());
            trial.setStatus("TRIAL");
            trial.setAdoptionRequest(req);
            trial.setStartDate(LocalDate.now());
            entityManager.persist(trial);
            trialList.add(trial);
        }

        adoptionRequest = factory.manufacturePojo(AdoptionRequestEntity.class);
        adoptionRequest.setPet(pet);
        adoptionRequest.setAdopter(adopter);
        adoptionRequest.setRequestDate(LocalDate.now());
        entityManager.persist(adoptionRequest);
    }

    @Test
    void testCreateTrialCohabitation() throws co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        TrialCohabitationEntity newTrial = new TrialCohabitationEntity();
        newTrial.setCreationDate(LocalDate.now());
        newTrial.setStatus("PENDING");
        newTrial.setAdoptionRequest(adoptionRequest);
        newTrial.setStartDate(LocalDate.now());

        TrialCohabitationEntity result = trialCohabitationService.createTrialCohabitation(newTrial);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testCreateTrialCohabitationNull() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> trialCohabitationService.createTrialCohabitation(null));
    }

    @Test
    void testCreateTrialCohabitationWithoutStartDate() {
        TrialCohabitationEntity trial = new TrialCohabitationEntity();
        trial.setCreationDate(LocalDate.now());
        trial.setAdoptionRequest(adoptionRequest);
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> trialCohabitationService.createTrialCohabitation(trial));
    }

    @Test
    void testSearchTrialCohabitation() throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        TrialCohabitationEntity expected = trialList.get(0);
        TrialCohabitationEntity result = trialCohabitationService.searchTrialCohabitation(expected.getId());
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchTrialCohabitationNotFound() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> trialCohabitationService.searchTrialCohabitation(0L));
    }

    @Test
    void testSearchTrialCohabitations() {
        List<TrialCohabitationEntity> results = trialCohabitationService.searchTrialCohabitations();
        assertTrue(results.size() >= trialList.size());
    }

    @Test
    void testUpdateTrialCohabitation() throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        TrialCohabitationEntity expected = trialList.get(0);
        TrialCohabitationEntity updatedInfo = new TrialCohabitationEntity();
        updatedInfo.setStartDate(LocalDate.now().minusDays(2));
        updatedInfo.setEndDate(LocalDate.now());
        updatedInfo.setNotes("Has behaved well");
        updatedInfo.setApproved(true);

        TrialCohabitationEntity result = trialCohabitationService.updateTrialCohabitation(expected.getId(), updatedInfo);
        assertNotNull(result);
        assertTrue(result.getApproved());
        assertEquals("Has behaved well", result.getNotes());
    }

    @Test
        void testDeleteTrialCohabitation()
            throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException,
            co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        TrialCohabitationEntity expected = trialList.get(0);
        trialCohabitationService.deleteTrialCohabitation(expected.getId());
        
        TrialCohabitationEntity deleted = entityManager.find(TrialCohabitationEntity.class, expected.getId());
        assertNull(deleted);
    }

    @Test
    void testUpdateTrialCohabitationNotFound() {
        TrialCohabitationEntity updatedInfo = new TrialCohabitationEntity();
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> trialCohabitationService.updateTrialCohabitation(0L, updatedInfo));
    }

    @Test
    void testDeleteTrialCohabitationNotFound() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> trialCohabitationService.deleteTrialCohabitation(0L));
    }
}
