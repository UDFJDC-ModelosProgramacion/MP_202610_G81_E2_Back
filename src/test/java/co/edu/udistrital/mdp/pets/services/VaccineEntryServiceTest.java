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

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({VaccineEntryService.class})
class VaccineEntryServiceTest {

    @Autowired
    private VaccineEntryService vaccineEntryService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<VaccineEntryEntity> vaccineList = new ArrayList<>();
    private MedicalHistoryEntity history;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VaccineEntryEntity").executeUpdate();
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

        VeterinarianEntity vet = new VeterinarianEntity();
        vet.setName("Test Vet");
        vet.setEmail("vetvaccine@test.com");
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
            VaccineEntryEntity vaccine = new VaccineEntryEntity();
            vaccine.setMedicalHistory(history);
            vaccine.setVaccineName("Vaccine " + i);
            vaccine.setAdminDate(LocalDate.now());
            vaccine.setNextDueDate(LocalDate.now().minusDays(1)); // Make it due
            entityManager.persist(vaccine);
            vaccineList.add(vaccine);
        }
    }

    @Test
    void testCreateVaccineEntry() throws EntityNotFoundException {
        VaccineEntryEntity newVaccine = new VaccineEntryEntity();
        newVaccine.setVaccineName("Rabies");
        newVaccine.setAdminDate(LocalDate.now());
        newVaccine.setNextDueDate(LocalDate.now().plusYears(1));

        VaccineEntryEntity result = vaccineEntryService.createVaccineEntry(history.getId(), newVaccine);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Rabies", result.getVaccineName());
    }

    @Test
    void testCreateVaccineEntryHistoryNotFound() {
        VaccineEntryEntity newVaccine = new VaccineEntryEntity();
        newVaccine.setVaccineName("Rabies");
        assertThrows(EntityNotFoundException.class, () -> 
            vaccineEntryService.createVaccineEntry(0L, newVaccine));
    }

    @Test
    void testGetVaccineEntries() {
        List<VaccineEntryEntity> results = vaccineEntryService.getVaccineEntries(history.getId());
        assertTrue(results.size() >= vaccineList.size());
    }

    @Test
    void testUpdateVaccineEntry() throws EntityNotFoundException {
        VaccineEntryEntity expected = vaccineList.get(0);
        VaccineEntryEntity updatedInfo = new VaccineEntryEntity();
        updatedInfo.setVaccineName("Distemper");
        updatedInfo.setAdminDate(LocalDate.now());
        updatedInfo.setNextDueDate(LocalDate.now().plusMonths(6));

        VaccineEntryEntity result = vaccineEntryService.updateVaccineEntry(expected.getId(), updatedInfo);
        assertNotNull(result);
        assertEquals("Distemper", result.getVaccineName());
    }

    @Test
    void testUpdateVaccineEntryNotFound() {
        VaccineEntryEntity newVaccine = new VaccineEntryEntity();
        newVaccine.setVaccineName("Rabies");
        assertThrows(EntityNotFoundException.class, () -> 
            vaccineEntryService.updateVaccineEntry(0L, newVaccine));
    }

    @Test
    void testFindVaccinesDue() {
        // We set 3 vaccines to have due dates in the past during setup
        List<VaccineEntryEntity> results = vaccineEntryService.findVaccinesDue();
        assertTrue(results.size() >= 3);
    }
}
