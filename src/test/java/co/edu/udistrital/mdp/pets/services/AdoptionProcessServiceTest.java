package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({AdoptionProcessService.class})
class AdoptionProcessServiceTest {

    @Autowired
    private AdoptionProcessService adoptionProcessService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<TrialCohabitationEntity> processList = new ArrayList<>();
    private AdoptionRequestEntity adoptionRequest;

    @BeforeEach
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
        entityManager.getEntityManager().createQuery("delete from ReturnCaseEntity").executeUpdate();
    }

    private void insertData() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        AdopterEntity adopter = new AdopterEntity();
        adopter.setName("Test Adopter");
        adopter.setEmail("adopterproc@test.com");
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

            TrialCohabitationEntity process = new TrialCohabitationEntity();
            process.setCreationDate(LocalDate.now());
            process.setStatus("IN_PROGRESS");
            process.setAdoptionRequest(req);
            process.setStartDate(LocalDate.now());
            entityManager.persist(process);
            processList.add(process);
        }
        
        adoptionRequest = factory.manufacturePojo(AdoptionRequestEntity.class);
        adoptionRequest.setPet(pet);
        adoptionRequest.setAdopter(adopter);
        adoptionRequest.setRequestDate(LocalDate.now());
        entityManager.persist(adoptionRequest);
    }

    @Test
    void testCreateAdoptionProcess() throws Exception {
        TrialCohabitationEntity newProcess = new TrialCohabitationEntity();
        newProcess.setCreationDate(LocalDate.now());
        newProcess.setStatus("PENDING");
        newProcess.setAdoptionRequest(adoptionRequest);
        newProcess.setStartDate(LocalDate.now());

        AdoptionProcessEntity result = adoptionProcessService.createAdoptionProcess(newProcess);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testCreateAdoptionProcessNull() throws Exception {
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionProcessService.createAdoptionProcess(null));
    }

    @Test
    void testCreateAdoptionProcessWithoutDate() throws Exception {
        TrialCohabitationEntity process = new TrialCohabitationEntity();
        process.setAdoptionRequest(adoptionRequest);
        process.setStartDate(LocalDate.now());
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionProcessService.createAdoptionProcess(process));
    }

    @Test
    void testCreateAdoptionProcessWithoutRequest() throws Exception {
        TrialCohabitationEntity process = new TrialCohabitationEntity();
        process.setCreationDate(LocalDate.now());
        process.setStartDate(LocalDate.now());
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionProcessService.createAdoptionProcess(process));
    }

    @Test
    void testSearchAdoptionProcess() throws Exception {
        AdoptionProcessEntity expected = processList.get(0);
        AdoptionProcessEntity result = adoptionProcessService.searchAdoptionProcess(expected.getId());
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchAdoptionProcessNotFound() throws Exception {
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionProcessService.searchAdoptionProcess(0L));
    }

    @Test
    void testSearchAdoptionProcesses() throws Exception {
        List<AdoptionProcessEntity> results = adoptionProcessService.searchAdoptionProcesses();
        assertTrue(results.size() >= processList.size());
    }

    @Test
    void testUpdateAdoptionProcess() throws Exception {
        AdoptionProcessEntity expected = processList.get(0);
        expected.setStatus("COMPLETED");

        AdoptionProcessEntity result = adoptionProcessService.updateAdoptionProcess(expected.getId(), expected);
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void testDeleteAdoptionProcess() throws Exception {
        AdoptionProcessEntity expected = processList.get(0);
        adoptionProcessService.deleteAdoptionProcess(expected.getId());
        
        AdoptionProcessEntity deleted = entityManager.find(TrialCohabitationEntity.class, expected.getId());
        assertTrue(deleted == null);
    }

    @Test
    void testDeleteAdoptionProcessWithReturnCase() throws Exception {
        AdoptionProcessEntity expected = processList.get(0);
        
        ReturnCaseEntity returnCase = new ReturnCaseEntity();
        returnCase.setAdoptionProcess(expected);
        returnCase.setReturnDate(LocalDate.now());
        entityManager.persist(returnCase);

        expected.setReturnCase(returnCase);
        entityManager.persist(expected);

        assertThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionProcessService.deleteAdoptionProcess(expected.getId()));
    }

    @Test
    void testUpdateAdoptionProcessNotFound() {
        AdoptionProcessEntity update = new TrialCohabitationEntity();
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionProcessService.updateAdoptionProcess(0L, update));
    }

    @Test
    void testDeleteAdoptionProcessNotFound() {
        assertThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionProcessService.deleteAdoptionProcess(0L));
    }
}
