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
@Import({AdoptionRequestService.class})
class AdoptionRequestServiceTest {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<AdoptionRequestEntity> requestList = new ArrayList<>();
    private AdopterEntity adopter;
    private PetEntity pet;

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

        pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        adopter = new AdopterEntity();
        adopter.setName("Test Adopter");
        adopter.setEmail("adopter@test.com");
        adopter.setPassword("pass");
        adopter.setPhoneNumber("123");
        adopter.setAddress("Calle 123");
        entityManager.persist(adopter);

        for (int i = 0; i < 3; i++) {
            AdoptionRequestEntity req = factory.manufacturePojo(AdoptionRequestEntity.class);
            req.setPet(pet);
            req.setAdopter(adopter);
            req.setRequestDate(LocalDate.now());
            req.setStatus("PENDING");
            entityManager.persist(req);
            requestList.add(req);
        }
    }

    @Test
    void testCreateAdoptionRequest() throws co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        AdoptionRequestEntity newReq = new AdoptionRequestEntity();
        newReq.setRequestDate(LocalDate.now());
        newReq.setPet(pet);
        newReq.setAdopter(adopter);
        newReq.setStatus("PENDING");

        AdoptionRequestEntity result = adoptionRequestService.createAdoptionRequest(newReq);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testCreateAdoptionRequestNull() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionRequestService.createAdoptionRequest(null));
    }

    @Test
    void testCreateAdoptionRequestWithoutDate() {
        AdoptionRequestEntity req = new AdoptionRequestEntity();
        req.setPet(pet);
        req.setAdopter(adopter);
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionRequestService.createAdoptionRequest(req));
    }

    @Test
    void testCreateAdoptionRequestWithoutPet() {
        AdoptionRequestEntity req = new AdoptionRequestEntity();
        req.setRequestDate(LocalDate.now());
        req.setAdopter(adopter);
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionRequestService.createAdoptionRequest(req));
    }

    @Test
    void testCreateAdoptionRequestWithoutAdopter() {
        AdoptionRequestEntity req = new AdoptionRequestEntity();
        req.setRequestDate(LocalDate.now());
        req.setPet(pet);
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionRequestService.createAdoptionRequest(req));
    }

    @Test
    void testSearchAdoptionRequest() throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        AdoptionRequestEntity expected = requestList.get(0);
        AdoptionRequestEntity result = adoptionRequestService.searchAdoptionRequest(expected.getId());
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchAdoptionRequestNotFound() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionRequestService.searchAdoptionRequest(0L));
    }

    @Test
    void testSearchAdoptionRequests() {
        List<AdoptionRequestEntity> results = adoptionRequestService.searchAdoptionRequests();
        assertTrue(results.size() >= requestList.size());
    }

    @Test
    void testUpdateAdoptionRequest() throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        AdoptionRequestEntity expected = requestList.get(0);
        AdoptionRequestEntity updatedInfo = new AdoptionRequestEntity();
        updatedInfo.setStatus("APPROVED");
        updatedInfo.setRequestDate(LocalDate.now().minusDays(2));

        AdoptionRequestEntity result = adoptionRequestService.updateAdoptionRequest(expected.getId(), updatedInfo);
        assertNotNull(result);
        assertEquals("APPROVED", result.getStatus());
    }

    @Test
        void testDeleteAdoptionRequest()
            throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException,
            co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        AdoptionRequestEntity expected = requestList.get(0);
        adoptionRequestService.deleteAdoptionRequest(expected.getId());
        
        AdoptionRequestEntity deleted = entityManager.find(AdoptionRequestEntity.class, expected.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteAdoptionRequestWithProcess() {
        AdoptionRequestEntity expected = requestList.get(0);
        
        TrialCohabitationEntity process = new TrialCohabitationEntity();
        process.setAdoptionRequest(expected);
        process.setCreationDate(LocalDate.now());
        process.setStartDate(LocalDate.now());
        entityManager.persist(process);

        expected.setAdoptionProcess(process);
        entityManager.persist(expected);

        expectThrows(co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException.class, () -> adoptionRequestService.deleteAdoptionRequest(expected.getId()));
    }

    @Test
    void testUpdateAdoptionRequestNotFound() {
        AdoptionRequestEntity update = new AdoptionRequestEntity();
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionRequestService.updateAdoptionRequest(0L, update));
    }

    @Test
    void testDeleteAdoptionRequestNotFound() {
        expectThrows(co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException.class, () -> adoptionRequestService.deleteAdoptionRequest(0L));
    }
}
