package co.edu.udistrital.mdp.pets.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterService.class)
class ShelterServiceTest {

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private final List<ShelterEntity> shelterList = new ArrayList<>();

    private <T extends Throwable> void expectThrows(Class<T> expectedType,
            org.junit.jupiter.api.function.Executable executable) {
        assertNotNull(org.junit.jupiter.api.Assertions.assertThrows(expectedType, executable));
    }

    @BeforeEach
    @SuppressWarnings({"java:S1144", "unused"})
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelterEntity = factory.manufacturePojo(ShelterEntity.class);
            shelterEntity.setNit("NIT-" + i + "-" + System.nanoTime());
            shelterEntity.setShelterName("Shelter-" + i + "-" + System.nanoTime());
            shelterEntity.setPhoneNumber("123456" + i);
            shelterEntity.setAddress("Address " + i);
            shelterEntity.setStatus("Activo");
            entityManager.persist(shelterEntity);
            shelterList.add(shelterEntity);
        }

        PetEntity petEntity = factory.manufacturePojo(PetEntity.class);
        petEntity.setShelter(shelterList.get(0));
        entityManager.persist(petEntity);
    }

    @Test
    void testCreateShelter() throws IllegalOperationException {
        ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
        newEntity.setNit("NIT-NUEVO-" + System.nanoTime());
        newEntity.setShelterName("Nuevo Refugio " + System.nanoTime());
        newEntity.setPhoneNumber("9876543");
        newEntity.setAddress("New Address");
        newEntity.setStatus("Activo");

        ShelterEntity result = shelterService.createShelter(newEntity);
        assertNotNull(result);

        ShelterEntity entity = entityManager.find(ShelterEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getNit(), entity.getNit());
        assertEquals(newEntity.getShelterName(), entity.getShelterName());
        assertEquals(newEntity.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(newEntity.getAddress(), entity.getAddress());
    }

    @Test
    void testCreateShelterMissingNit() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit(null);
            newEntity.setShelterName("Refugio Test");
            newEntity.setPhoneNumber("1234567");
            newEntity.setAddress("Address Test");
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testCreateShelterMissingName() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit("NIT-UNICO-" + System.nanoTime());
            newEntity.setShelterName(null);
            newEntity.setPhoneNumber("1234567");
            newEntity.setAddress("Address Test");
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testCreateShelterMissingPhoneNumber() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit("NIT-UNICO-" + System.nanoTime());
            newEntity.setShelterName("Refugio " + System.nanoTime());
            newEntity.setPhoneNumber(null);
            newEntity.setAddress("Address Test");
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testCreateShelterMissingAddress() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit("NIT-UNICO-" + System.nanoTime());
            newEntity.setShelterName("Refugio " + System.nanoTime());
            newEntity.setPhoneNumber("1234567");
            newEntity.setAddress("   ");
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testCreateShelterDuplicateName() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit("NIT-UNICO-" + System.nanoTime());
            newEntity.setShelterName(shelterList.get(0).getShelterName());
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testCreateShelterNullStatus() throws IllegalOperationException {
        ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
        newEntity.setNit("NIT-UNICO-" + System.nanoTime());
        newEntity.setShelterName("Refugio Null " + System.nanoTime());
        newEntity.setPhoneNumber("1234567");
        newEntity.setAddress("Street XYZ");
        newEntity.setStatus(null);
        ShelterEntity saved = shelterService.createShelter(newEntity);
        assertEquals("Activo", saved.getStatus());
    }

    @Test
    void testCreateShelterDuplicateNit() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = factory.manufacturePojo(ShelterEntity.class);
            newEntity.setNit(shelterList.get(0).getNit());
            newEntity.setShelterName("Otro Refugio " + System.nanoTime());
            newEntity.setPhoneNumber("1234567");
            newEntity.setAddress("Address Test");
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testSearchShelter() throws EntityNotFoundException {
        ShelterEntity entity = shelterList.get(0);
        ShelterEntity resultEntity = shelterService.searchShelter(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getNit(), resultEntity.getNit());
        assertEquals(entity.getShelterName(), resultEntity.getShelterName());
    }

    @Test
    void testSearchShelterNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            shelterService.searchShelter(0L);
        });
    }

    @Test
    void testSearchShelters() {
        List<ShelterEntity> list = shelterService.searchShelters();
        assertEquals(shelterList.size(), list.size());
        for (ShelterEntity entity : list) {
            boolean found = false;
            for (ShelterEntity storedEntity : shelterList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testSearchSheltersByName() {
        List<ShelterEntity> result = shelterService.searchSheltersByName(shelterList.get(0).getShelterName());
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSearchSheltersByNameNotFound() {
        List<ShelterEntity> result = shelterService.searchSheltersByName("Unknown");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateShelter() throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity entity = shelterList.get(0);
        ShelterEntity updatedShelter = factory.manufacturePojo(ShelterEntity.class);
        updatedShelter.setNit(entity.getNit());
        updatedShelter.setStatus("Activo");
        updatedShelter.setShelterName("Nombre Actualizado " + System.nanoTime());

        ShelterEntity result = shelterService.updateShelter(entity.getId(), updatedShelter);
        assertNotNull(result);

        ShelterEntity resp = entityManager.find(ShelterEntity.class, entity.getId());
        assertEquals(updatedShelter.getShelterName(), resp.getShelterName());
    }

    @Test
    void testUpdateShelterNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            ShelterEntity updatedShelter = factory.manufacturePojo(ShelterEntity.class);
            shelterService.updateShelter(0L, updatedShelter);
        });
    }

    @Test
    void testUpdateShelterChangeNit() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity entity = shelterList.get(0);
            ShelterEntity updatedShelter = factory.manufacturePojo(ShelterEntity.class);
            updatedShelter.setNit("NIT-DIFERENTE-" + System.nanoTime());
            shelterService.updateShelter(entity.getId(), updatedShelter);
        });
    }

    @Test
    void testUpdateShelterNotActive() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity entity = shelterList.get(0);
            entity.setStatus("Inactivo");
            entityManager.merge(entity);

            ShelterEntity updatedShelter = factory.manufacturePojo(ShelterEntity.class);
            updatedShelter.setNit(entity.getNit());
            shelterService.updateShelter(entity.getId(), updatedShelter);
        });
    }

    @Test
    void testUpdateShelterDuplicateName() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity entity = shelterList.get(0);
            ShelterEntity updatedShelter = factory.manufacturePojo(ShelterEntity.class);
            updatedShelter.setNit(entity.getNit());
            updatedShelter.setStatus("Activo");
            updatedShelter.setShelterName(shelterList.get(1).getShelterName());
            shelterService.updateShelter(entity.getId(), updatedShelter);
        });
    }

    @Test
    void testDeleteShelter() throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity entity = shelterList.get(1);
        shelterService.deleteShelter(entity.getId());
        ShelterEntity deleted = entityManager.find(ShelterEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteShelterNotFound() {
        expectThrows(EntityNotFoundException.class, () -> {
            shelterService.deleteShelter(0L);
        });
    }

    @Test
    void testDeleteShelterWithPets() {
        expectThrows(IllegalOperationException.class, () -> {
            ShelterEntity entity = shelterList.get(0);
            shelterService.deleteShelter(entity.getId());
        });
    }
}
