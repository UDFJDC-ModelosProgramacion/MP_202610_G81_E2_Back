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

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({MedicalEventService.class})
class MedicalEventServiceTest {

    @Autowired
    private MedicalEventService medicalEventService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<MedicalEventEntity> eventList = new ArrayList<>();
    private MedicalHistoryEntity history;
    private VeterinarianEntity vet;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MedicalHistoryEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from UserEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelter);

        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        pet.setShelter(shelter);
        entityManager.persist(pet);

        vet = new VeterinarianEntity();
        vet.setName("Test Vet");
        vet.setEmail("vet@test.com");
        vet.setPassword("pass");
        vet.setPhoneNumber("123");
        vet.setLicenseNumber("LIC123");
        vet.setShelter(shelter);
        entityManager.persist(vet);

        history = new MedicalHistoryEntity();
        history.setPet(pet);
        history.setVeterinarian(vet);
        history.setCreatedDate(LocalDate.now());
        history.setLastUpdated(LocalDate.now());
        entityManager.persist(history);

        for (int i = 0; i < 3; i++) {
            MedicalEventEntity event = new MedicalEventEntity();
            event.setMedicalHistory(history);
            event.setVeterinarian(vet);
            event.setEventType("Checkup");
            event.setDescription("Periodic checkup " + i);
            event.setEventDate(LocalDate.now());
            entityManager.persist(event);
            eventList.add(event);
        }
    }

    @Test
    void testCreateMedicalEvent() throws Exception {
        MedicalEventEntity newEvent = new MedicalEventEntity();
        newEvent.setEventType("Surgery");
        newEvent.setDescription("Surgery procedure");
        newEvent.setEventDate(LocalDate.now());

        MedicalEventEntity result = medicalEventService.createMedicalEvent(history.getId(), vet.getId(), newEvent);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Surgery", result.getEventType());
    }

    @Test
    void testCreateMedicalEventHistoryNotFound() throws Exception {
        MedicalEventEntity event = new MedicalEventEntity();
        event.setEventType("Surgery");
        assertThrows(EntityNotFoundException.class, () -> 
            medicalEventService.createMedicalEvent(0L, vet.getId(), event));
    }

    @Test
    void testCreateMedicalEventVetNotFound() throws Exception {
        MedicalEventEntity event = new MedicalEventEntity();
        event.setEventType("Surgery");
        assertThrows(EntityNotFoundException.class, () -> 
            medicalEventService.createMedicalEvent(history.getId(), 0L, event));
    }

    @Test
    void testSearchMedicalEvent() throws Exception {
        MedicalEventEntity expected = eventList.get(0);
        MedicalEventEntity result = medicalEventService.searchMedicalEvent(expected.getId());
        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void testSearchMedicalEventNotFound() throws Exception {
        assertThrows(EntityNotFoundException.class, () -> 
            medicalEventService.searchMedicalEvent(0L));
    }

    @Test
    void testSearchMedicalEvents() throws Exception {
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(1);
        List<MedicalEventEntity> results = medicalEventService.searchMedicalEvents(start, end);
        assertTrue(results.size() >= eventList.size());
    }

    @Test
    void testUpdateMedicalEvent() throws Exception {
        MedicalEventEntity expected = eventList.get(0);
        MedicalEventEntity updatedInfo = new MedicalEventEntity();
        updatedInfo.setEventType("Vaccination");
        updatedInfo.setDescription("Rabies");
        updatedInfo.setEventDate(LocalDate.now().plusDays(1));

        MedicalEventEntity result = medicalEventService.updateMedicalEvent(expected.getId(), updatedInfo);
        assertNotNull(result);
        assertEquals("Vaccination", result.getEventType());
        assertEquals("Rabies", result.getDescription());
    }

    @Test
    void testDeleteMedicalEvent() throws Exception {
        MedicalEventEntity expected = eventList.get(0);
        medicalEventService.deleteMedicalEvent(expected.getId());
        
        MedicalEventEntity deleted = entityManager.find(MedicalEventEntity.class, expected.getId());
        assertNull(deleted);
    }
}
