package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(VeterinarianService.class)
class VeterinarianServiceTest {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<VeterinarianEntity> veterinarianList = new ArrayList<>();
    private final List<ShelterEntity> shelterList = new ArrayList<>();
    private final List<VetSpecialityEntity> specialityList = new ArrayList<>();

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
        entityManager.getEntityManager().createQuery("delete from MedicalEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from MedicalHistoryEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VetSpecialityEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelterEntity = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelterEntity);
            shelterList.add(shelterEntity);
        }

        for (int i = 0; i < 3; i++) {
            VetSpecialityEntity specialityEntity = factory.manufacturePojo(VetSpecialityEntity.class);
            specialityEntity.setName("Especialidad-" + i + "-" + System.nanoTime());
            specialityEntity.setVeterinarians(new ArrayList<>());
            entityManager.persist(specialityEntity);
            specialityList.add(specialityEntity);
        }

        for (int i = 0; i < 3; i++) {
            VeterinarianEntity veterinarianEntity = factory.manufacturePojo(VeterinarianEntity.class);
            veterinarianEntity.setLicenseNumber("LIC-" + i + "-" + System.nanoTime());
            veterinarianEntity.setShelter(shelterList.get(0));
            veterinarianEntity.setSpecialities(new ArrayList<>());
            veterinarianEntity.setMedicalHistories(new ArrayList<>());
            veterinarianEntity.setMedicalEvents(new ArrayList<>());
            entityManager.persist(veterinarianEntity);
            veterinarianList.add(veterinarianEntity);
        }

        veterinarianList.get(0).getSpecialities().add(specialityList.get(0));
        specialityList.get(0).getVeterinarians().add(veterinarianList.get(0));
    }

    @Test
    void testCreateVeterinarian() throws IllegalOperationException, EntityNotFoundException {
        VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
        newEntity.setLicenseNumber("LIC-NEW-" + System.nanoTime());
        newEntity.setSpecialities(new ArrayList<>());
        newEntity.setMedicalHistories(new ArrayList<>());
        newEntity.setMedicalEvents(new ArrayList<>());
        entityManager.persist(newEntity);

        VeterinarianEntity result = veterinarianService.createVeterinarian(newEntity);
        assertNotNull(result);

        VeterinarianEntity entity = entityManager.find(VeterinarianEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getLicenseNumber(), entity.getLicenseNumber());
    }

    @Test
    void testCreateVeterinarianNoLicense() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
            newEntity.setLicenseNumber(null);
            newEntity.setSpecialities(new ArrayList<>());
            newEntity.setMedicalHistories(new ArrayList<>());
            newEntity.setMedicalEvents(new ArrayList<>());
            veterinarianService.createVeterinarian(newEntity);
        });
    }

    @Test
    void testCreateVeterinarianEmptyLicense() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
            newEntity.setLicenseNumber("   ");
            newEntity.setSpecialities(new ArrayList<>());
            newEntity.setMedicalHistories(new ArrayList<>());
            newEntity.setMedicalEvents(new ArrayList<>());
            veterinarianService.createVeterinarian(newEntity);
        });
    }

    @Test
    void testCreateVeterinarianNoId() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
            newEntity.setId(null);
            newEntity.setLicenseNumber("LIC-TEST");
            newEntity.setSpecialities(new ArrayList<>());
            newEntity.setMedicalHistories(new ArrayList<>());
            newEntity.setMedicalEvents(new ArrayList<>());
            veterinarianService.createVeterinarian(newEntity);
        });
    }

    @Test
    void testCreateVeterinarianUserNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            VeterinarianEntity newEntity = factory.manufacturePojo(VeterinarianEntity.class);
            newEntity.setId(0L);
            newEntity.setLicenseNumber("LIC-TEST");
            newEntity.setSpecialities(new ArrayList<>());
            newEntity.setMedicalHistories(new ArrayList<>());
            newEntity.setMedicalEvents(new ArrayList<>());
            veterinarianService.createVeterinarian(newEntity);
        });
    }

    @Test
    void testSearchVeterinarian() throws EntityNotFoundException {
        VeterinarianEntity entity = veterinarianList.get(0);
        VeterinarianEntity resultEntity = veterinarianService.searchVeterinarian(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getLicenseNumber(), resultEntity.getLicenseNumber());
    }

    @Test
    void testSearchVeterinarianNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            veterinarianService.searchVeterinarian(0L);
        });
    }

    @Test
    void testSearchVeterinarians() {
        List<VeterinarianEntity> list = veterinarianService.searchVeterinarians();
        assertEquals(veterinarianList.size(), list.size());
        for (VeterinarianEntity entity : list) {
            boolean found = false;
            for (VeterinarianEntity storedEntity : veterinarianList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testSearchVeterinariansBySpeciality() {
        List<VeterinarianEntity> list = veterinarianService.searchVeterinariansBySpeciality(specialityList.get(0).getId());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testSearchVeterinariansByShelter() {
        List<VeterinarianEntity> list = veterinarianService.searchVeterinariansByShelter(shelterList.get(0).getId());
        assertNotNull(list);
        assertFalse(list.isEmpty());
    }

    @Test
    void testUpdateVeterinarian() throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity entity = veterinarianList.get(0);
        VeterinarianEntity updatedVeterinarian = new VeterinarianEntity();
        updatedVeterinarian.setId(entity.getId());
        updatedVeterinarian.setEmail("new@test.com");

        VeterinarianEntity result = veterinarianService.updateVeterinarian(entity.getId(), updatedVeterinarian);
        assertNotNull(result);

        VeterinarianEntity resp = entityManager.find(VeterinarianEntity.class, entity.getId());
        assertEquals("new@test.com", resp.getEmail());
    }

    @Test
    void testUpdateVeterinarianNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            VeterinarianEntity updatedVeterinarian = new VeterinarianEntity();
            updatedVeterinarian.setId(0L);
            veterinarianService.updateVeterinarian(0L, updatedVeterinarian);
        });
    }

    @Test
    void testUpdateVeterinarianChangeIdFails() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity entity = veterinarianList.get(0);
            VeterinarianEntity updatedVeterinarian = new VeterinarianEntity();
            updatedVeterinarian.setId(999L);
            veterinarianService.updateVeterinarian(entity.getId(), updatedVeterinarian);
        });
    }

    @Test
    void testDeleteVeterinarian() throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity entity = veterinarianList.get(1);
        veterinarianService.deleteVeterinarian(entity.getId());
        VeterinarianEntity deleted = entityManager.find(VeterinarianEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteVeterinarianNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            veterinarianService.deleteVeterinarian(0L);
        });
    }

    @Test
    void testDeleteVeterinarianWithMedicalHistory() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity entity = veterinarianList.get(0);
            MedicalHistoryEntity history = factory.manufacturePojo(MedicalHistoryEntity.class);
            history.setVeterinarian(entity);
            entityManager.persist(history);
            entity.getMedicalHistories().add(history);
            entityManager.merge(entity);
            veterinarianService.deleteVeterinarian(entity.getId());
        });
    }

    @Test
    void testDeleteVeterinarianWithMedicalEvents() {
        expectThrows(IllegalOperationException.class, () -> {
            VeterinarianEntity entity = veterinarianList.get(0);
            MedicalEventEntity event = factory.manufacturePojo(MedicalEventEntity.class);
            event.setVeterinarian(entity);
            entityManager.persist(event);
            entity.getMedicalEvents().add(event);
            entityManager.merge(entity);
            veterinarianService.deleteVeterinarian(entity.getId());
        });
    }
}
